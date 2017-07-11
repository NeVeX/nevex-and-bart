package com.mark.nevexandbart.model.bartapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by NeVeX on 9/5/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StationWithEstimateEntryDto extends StationEntryDto {

    @XmlElement(name = "etd")
    private List<DestinationEstimateWrapperDto> etd;

    public List<DestinationEstimateWrapperDto> getEtd() {
        return etd;
    }

    public void setEtd(List<DestinationEstimateWrapperDto> etd) {
        this.etd = etd;
    }
}
