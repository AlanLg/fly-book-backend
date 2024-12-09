package com.flybook.controller;

import com.flybook.model.parse.Currency;
import com.flybook.service.impl.CurrencyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyServiceImpl currencyServiceImpl;

    @GetMapping("/fetch-rates")
    public ResponseEntity<List<Currency>> fetchRates() {
        return ResponseEntity.ok(currencyServiceImpl.fetchAndParseXML());
    }
}

