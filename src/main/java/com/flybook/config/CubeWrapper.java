package com.flybook.config;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CubeWrapper {

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private TimeCube timeCube;

    public TimeCube getTimeCube() {
        return timeCube;
    }

    public void setTimeCube(TimeCube timeCube) {
        this.timeCube = timeCube;
    }
}
