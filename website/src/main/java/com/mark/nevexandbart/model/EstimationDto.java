package com.mark.nevexandbart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by NeVeX on 9/5/2016.
 */
public class EstimationDto implements Serializable {

    @JsonProperty("origin_station")
    private String originStation;

    @JsonProperty("entries")
    private Set<EstimationEntryDto> entries;

    public String getOriginStation() {
        return originStation;
    }

    public void setOriginStation(String originStation) {
        this.originStation = originStation;
    }

    public Set<EstimationEntryDto> getEntries() {
        return entries;
    }

    public void setEntries(Set<EstimationEntryDto> entries) {
        this.entries = entries;
    }
}
