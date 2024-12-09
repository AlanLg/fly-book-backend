package com.flybook.controller;

import com.flybook.config.Envelope;
import com.flybook.service.impl.XMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XMLController {
    @Autowired
    private XMLService xmlService;

    @GetMapping("/fetch-rates")
    public Envelope fetchRates() {
        return xmlService.fetchAndParseXML();
    }
}

