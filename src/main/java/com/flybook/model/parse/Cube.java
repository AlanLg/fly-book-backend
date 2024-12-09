package com.flybook.model.parse;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Cube {
    @XmlAttribute(name = "time")
    private String time;
    @XmlElement(name = "Cube")
    private List<Currency> currencies;
}

