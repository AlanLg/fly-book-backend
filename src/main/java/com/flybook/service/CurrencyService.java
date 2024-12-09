package com.flybook.service;

import com.flybook.model.parse.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> fetchAndParseCurrencyXML();
}
