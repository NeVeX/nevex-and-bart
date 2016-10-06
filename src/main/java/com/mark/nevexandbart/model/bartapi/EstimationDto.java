package com.mark.nevexandbart.model.bartapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by NeVeX on 9/5/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EstimationDto {

    @XmlElement(name = "minutes")
    private Integer minutes;
    @XmlElement(name = "direction")
    private String direction;
    @XmlElement(name = "hexcolor")
    private String hexColor;

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

    //    <etd>
//    <destination>Fremont</destination>
//    <abbreviation>FRMT</abbreviation>
//    <limited>0</limited>
//    <estimate>
//    <minutes>12</minutes>
//    <platform>2</platform>
//    <direction>South</direction>
//    <length>6</length>
//    <color>ORANGE</color>
//    <hexcolor>#ff9933</hexcolor>
//    <bikeflag>1</bikeflag>
//    </estimate>
//
}
