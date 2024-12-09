package com.flybook.service.impl;

import com.flybook.model.parse.Envelope;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;

@Service
public class XMLService {
    public Envelope fetchAndParseXML() {
        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

        try {
            RestTemplate restTemplate = new RestTemplate();
            String xmlData = restTemplate.getForObject(url, String.class);

            if (xmlData != null) {
                JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                return (Envelope) unmarshaller.unmarshal(new StringReader(xmlData));
            }
        } catch (JAXBException e) {
            throw new RuntimeException("Error during XML parsing: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching XML data: " + e.getMessage());
        }
        return null;
    }
}

