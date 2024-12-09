package com.flybook.controller;

import com.flybook.model.parse.Currency;
import com.flybook.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/fetch-rates")
    public ResponseEntity<List<Currency>> fetchRates() {
        return ResponseEntity.ok(currencyService.fetchAndParseCurrencyXML());
    }
}

