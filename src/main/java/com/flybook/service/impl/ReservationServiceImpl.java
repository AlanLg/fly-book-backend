package com.flybook.service.impl;

import com.flybook.dbaccess.ProfileDbAccess;
import com.flybook.dbaccess.ReservationDbAccess;
import com.flybook.exception.FlybookException;
import com.flybook.mapper.ProfilMapper;
import com.flybook.mapper.ReservationMapper;
import com.flybook.model.dto.db.FlightDTO;
import com.flybook.model.dto.db.ProfileDTO;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.model.dto.db.ClientDTO;
import com.flybook.model.dto.db.ReservationDTO;
import com.flybook.service.ReservationService;
import com.flybook.utils.ReservationValidationUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDbAccess reservationDbAccess;
    private final ClientServiceImpl clientService;
    private final FlightServiceImpl flightService;
    private final ProfileDbAccess profileDbAccess;
    private final CurrencyServiceImpl currencyService;
    @Getter
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public ReservationDTOResponse createReservation(ReservationDTORequest reservationDTORequest, String email) {
        ClientDTO clientDTO = clientService.getClientForReservation(email);
        return finaliserReservation(reservationDTORequest, clientDTO);
    }

    @Override
    public List<ReservationDTOResponse> getAllReservationsForClient(String clientEmail) {
        return ReservationMapper.INSTANCE
                .reservationEntityToReservationDTOResponse(reservationDbAccess.findByClientEmail(clientEmail));
    }

    private ReservationDTOResponse finaliserReservation(ReservationDTORequest reservationDTORequest, ClientDTO clientDTO) {
        FlightDTO flightDTO = flightService.getFlightForReservation(reservationDTORequest);

        int numberOfSeatsForFlight = reservationDbAccess.countDistinctByFlightAndDepartureDate(flightDTO, reservationDTORequest.getDepartureDate());
        if (numberOfSeatsForFlight == flightDTO.getNumberOfSeats()) {
            Sinks.EmitResult result = sink.tryEmitNext("failed");
            if (result.isFailure()) {
                log.error("Failed to push event");
            }
            throw new FlybookException("The flight is full", HttpStatus.CONFLICT);
        }

        ReservationDTO createdReservationDTO = ReservationMapper.INSTANCE.clientEntityAndFlightEntityAndReservationDTORequestToReservationEntity(clientDTO, flightDTO, reservationDTORequest);
        if (!ReservationValidationUtils.isValidReservation(createdReservationDTO)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Optional<ReservationDTO> existingReservation = reservationDbAccess.findByFlightIdAndClientId(createdReservationDTO.getFlightDTO().getFlightId(), createdReservationDTO.getClientDTO().getId());

        if (existingReservation.isPresent()) {
            return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(existingReservation.get());
        }

        List<ProfileDTO> profileDTOS = ProfilMapper.INSTANCE.profilDTORequestListToProfilListEntity(reservationDTORequest.getProfilDTORequestList());
        createdReservationDTO.setPriceOfReservation(getTotalPriceOfReservation(flightDTO.getPrice(), profileDTOS, reservationDTORequest.getCurrency()));
        createdReservationDTO.setNbLuggage(profileDTOS.stream().mapToInt(ProfileDTO::getNbLuggage).sum());
        createdReservationDTO = reservationDbAccess.saveReservation(createdReservationDTO);

        for (ProfileDTO profileDTO : profileDTOS) {
            profileDTO.setReservationDTO(createdReservationDTO);
            profileDbAccess.saveProfile(profileDTO);
        }

        createdReservationDTO.setProfileDTOS(profileDTOS);

        Sinks.EmitResult result = sink.tryEmitNext("success");
        if (result.isFailure()) {
            log.error("Failed to push event");
        }
        return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(createdReservationDTO);
    }

    private double getTotalPriceOfReservation(double singleFlightPrice, List<ProfileDTO> profileDTOS, String currency) {
        Map<String, Double> currencies = currencyService.getCurrencies();
        double priceOfAllLuggage = profileDTOS.stream().mapToInt(ProfileDTO::getNbLuggage).sum() * 100;
        double totalFlightPrice = singleFlightPrice * profileDTOS.size();
        long amountOfChildren = profileDTOS.stream()
                .filter(this::isChild)
                .count();
        long amountOfAdult = profileDTOS.size() - amountOfChildren;

        if (profileDTOS.size() >= 4 && amountOfChildren >= 2 && amountOfAdult >= 2) {
            totalFlightPrice -= singleFlightPrice;
        }

        return (totalFlightPrice + priceOfAllLuggage) * currencies.get(currency);
    }

    private boolean isChild(ProfileDTO profileDTO) {
        return profileDTO.getBirthday().isBefore(LocalDate.now().minusYears(15));
    }
}
