package com.mark.nevexandbart.model.bartapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by NeVeX on 9/5/2016.
 */
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstimationWrapperDto {

    @XmlElement(name = "station")
    private StationWithEstimateEntryDto station;

    public StationWithEstimateEntryDto getStation() {
        return station;
    }

    public void setStation(StationWithEstimateEntryDto station) {
        this.station = station;
    }
}
