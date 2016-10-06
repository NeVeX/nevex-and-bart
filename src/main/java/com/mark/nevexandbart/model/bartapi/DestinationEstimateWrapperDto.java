package com.mark.nevexandbart.model.bartapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by NeVeX on 9/5/2016.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class DestinationEstimateWrapperDto {

    @XmlElement(name = "destination")
    private String destination;
    @XmlElement(name = "abbreviation")
    private String abbreviation;
    @XmlElement(name = "limited")
    private Integer limited;
    @XmlElement(name = "estimate")
    private List<EstimationDto> estimate;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Integer getLimited() {
        return limited;
    }

    public void setLimited(Integer limited) {
        this.limited = limited;
    }

    public List<EstimationDto> getEstimate() {
        return estimate;
    }

    public void setEstimate(List<EstimationDto> estimate) {
        this.estimate = estimate;
    }
}
