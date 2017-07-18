package com.mark.nevexandbart.service;

import com.mark.nevexandbart.model.EstimationDto;
import com.mark.nevexandbart.model.StationsDto;

import java.util.List;

/**
 * Created by Mark Cunningham on 7/18/2017.
 */
public interface BartApiService {

    /**
     * Gets the list of all stations that are operating in the Bart system
     */
    List<StationsDto> getStations();

    /**
     * For the given station, this will return all the available estimates
     */
    EstimationDto getEstimationForStation(String station);
}
