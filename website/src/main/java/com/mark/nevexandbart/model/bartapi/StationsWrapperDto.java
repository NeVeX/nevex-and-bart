package com.mark.nevexandbart.model.bartapi;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by NeVeX on 9/5/2016.
 */
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class StationsWrapperDto {

    @XmlElementWrapper(name = "stations")
    @XmlElement(name = "station")
    private List<StationEntryDto> stations;

    public List<StationEntryDto> getStations() {
        return stations;
    }

    public void setStations(List<StationEntryDto> stations) {
        this.stations = stations;
    }
}
