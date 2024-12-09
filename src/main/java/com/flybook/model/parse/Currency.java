package com.flybook.model.parse;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
public class Currency {

    @XmlAttribute(name = "currency")
    private String currency;

    @XmlAttribute(name = "rate")
    private String rate;
}


