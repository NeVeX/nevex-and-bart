package com.mark.nevexandbart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by NeVeX on 9/5/2016.
 */
@Configuration
public class BartProperties {

    @Value("${nevex.bart.api-stations-url}")
    private String bartStationsApiUrl;
    @Value("${nevex.bart.api-estimatation-url}")
    private String bartEstimationApiUrl;

    public String getBartStationsApiUrl() {
        return bartStationsApiUrl;
    }

    public void setBartStationsApiUrl(String bartStationsApiUrl) {
        this.bartStationsApiUrl = bartStationsApiUrl;
    }

    public String getBartEstimationApiUrl() {
        return bartEstimationApiUrl;
    }

    public void setBartEstimationApiUrl(String bartEstimationApiUrl) {
        this.bartEstimationApiUrl = bartEstimationApiUrl;
    }
}
