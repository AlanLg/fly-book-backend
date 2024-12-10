package com.flybook.controller;

import com.flybook.service.ReservationService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.time.Duration;
import java.util.stream.Stream;

@Controller
@RequestMapping("/realtime")
public class RealtimeController {
    private final ReservationService reservationService;

    public RealtimeController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(path = "/periodic", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getEvents() {
        return reservationService.getSink().asFlux().map(e -> ServerSentEvent.builder(e).build());
    }
}
