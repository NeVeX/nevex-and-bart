package com.mark.nevexandbart.model.bartapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by NeVeX on 9/5/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StationEntryDto {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "abbr")
    private String abbr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
}
