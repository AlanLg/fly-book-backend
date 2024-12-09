package com.flybook.model.parse;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Sender {

    @XmlElement(name = "name", namespace = "http://www.gesmes.org/xml/2002-08-01")
    private String name;
}

