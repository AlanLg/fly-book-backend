package com.flybook.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "flight_id")
    private Flight flight;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "reservation")
    private List<Profile> profiles;
    private LocalDate departureDate;
    private int nbLuggage;
    private double priceOfReservation;
    private LocalDateTime creationDate = LocalDateTime.now();
}