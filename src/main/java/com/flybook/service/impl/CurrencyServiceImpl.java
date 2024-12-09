package com.flybook.service.impl;

import com.flybook.model.parse.Currency;
import com.flybook.model.parse.Envelope;
import com.flybook.service.CurrencyService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private static final long ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;
    private static final String URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    private long lastUpdateTime = 0;
    private List<Currency> currencies;

    public List<Currency> fetchAndParseCurrencyXML() {
        if (System.currentTimeMillis() > this.lastUpdateTime + ONE_DAY_IN_MS) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String xmlData = restTemplate.getForObject(URL, String.class);

                log.info("xml data: {}", xmlData);

                if (xmlData != null) {
                    JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    Envelope unmarshal = (Envelope) unmarshaller.unmarshal(new StringReader(xmlData));
                    log.info("xml data parsed: {}", unmarshal);
                    List<Currency> currencies = unmarshal.getCubeWrapper().getTimeCube().getCurrencies();
                    currencies.add(new Currency("EUR", "1"));
                    this.currencies = currencies;
                    this.lastUpdateTime = System.currentTimeMillis();
                    return currencies;
                }
            } catch (JAXBException e) {
                throw new RuntimeException("Error during XML parsing: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException("Error fetching XML data: " + e.getMessage());
            }
        }
        return this.currencies;
    }

    public Map<String, Double> getCurrencies() {
        return currencies.stream().collect(Collectors.toMap(Currency::getCurrency, currency -> Double.valueOf(currency.getRate())));
    }
}

