package com.flybook.model.parse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;

public class XMLParser {
    public static void main(String[] args) {
        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        RestTemplate restTemplate = new RestTemplate();
        String xmlData = restTemplate.getForObject(url, String.class);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Envelope envelope = (Envelope) unmarshaller.unmarshal(new StringReader(xmlData));

            System.out.println("Subject: " + envelope.getSubject());
            System.out.println("Sender: " + envelope.getSender().getName());
            System.out.println("Time: " + envelope.getCubeWrapper().getTimeCube().getTime());
            envelope.getCubeWrapper().getTimeCube().getCurrencies()
                    .forEach(currency -> System.out.println(currency.getCurrency() + ": " + currency.getRate()));
        } catch (JAXBException e) {
            System.err.println("Error during XML parsing: " + e.getMessage());
        }
    }
}


