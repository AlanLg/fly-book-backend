package com.flybook.config;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "Envelope", namespace = "http://www.gesmes.org/xml/2002-08-01")
@XmlAccessorType(XmlAccessType.FIELD)
public class Envelope {

    @XmlElement(name = "subject", namespace = "http://www.gesmes.org/xml/2002-08-01")
    private String subject;

    @XmlElement(name = "Sender", namespace = "http://www.gesmes.org/xml/2002-08-01")
    private Sender sender;

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private CubeWrapper cubeWrapper;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public CubeWrapper getCubeWrapper() {
        return cubeWrapper;
    }

    public void setCubeWrapper(CubeWrapper cubeWrapper) {
        this.cubeWrapper = cubeWrapper;
    }
}


