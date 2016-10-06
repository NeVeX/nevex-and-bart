package com.mark.nevexandbart.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by NeVeX on 9/5/2016.
 */
public class StationsDto implements Serializable {

    @JsonProperty("name")
    private String stationName;
    @JsonProperty("abbreviation")
    private String abbreviation;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
