package com.mark.nevexandbart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by NeVeX on 9/5/2016.
 */
public class EstimationEntryDto implements Comparable<EstimationEntryDto>, Serializable {

    @JsonProperty("train_name")
    private String trainName;
    @JsonProperty("minutes")
    private Integer minutes;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("hex_color")
    private String hexColor;

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }


    @Override
    public int compareTo(EstimationEntryDto o) {
        int thisMinute = this.getMinutes() != null ? this.getMinutes() : 0;
        int thatMinute = o.getMinutes() != null ? o.getMinutes() : 0;

        if ( thisMinute == thatMinute) { return 0; }
        if ( thisMinute > thatMinute) { return 1; }
        return -1;
    }
}
