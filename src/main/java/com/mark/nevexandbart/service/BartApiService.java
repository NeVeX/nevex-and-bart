package com.mark.nevexandbart.service;

import com.mark.nevexandbart.config.BartProperties;
import com.mark.nevexandbart.model.EstimationDto;
import com.mark.nevexandbart.model.EstimationEntryDto;
import com.mark.nevexandbart.model.StationsDto;
import com.mark.nevexandbart.model.bartapi.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by NeVeX on 9/5/2016.
 */
@Service
public class BartApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final List<StationsDto> currentStations = new CopyOnWriteArrayList<>();
    private long lastUpdateToStationsMs = 0;
    private final String stationsApiUrl;
    private final String estimationApiUrl;

    public BartApiService(BartProperties props) {
        stationsApiUrl = props.getBartStationsApiUrl();
        estimationApiUrl = props.getBartEstimationApiUrl();
    }

    public List<StationsDto> getStations() {
        if ( currentStations.isEmpty() || (System.currentTimeMillis() - lastUpdateToStationsMs) > TimeUnit.DAYS.toMillis(1)) {
            List<StationsDto> newStations = getStationsFromApi();
            if ( newStations != null && !newStations.isEmpty()) {
                synchronized (currentStations) {
                    currentStations.clear();
                    currentStations.addAll(newStations);
                }
                lastUpdateToStationsMs = System.currentTimeMillis();
            }
        }
        return currentStations;
    }

    private List<StationsDto> getStationsFromApi() {
        ResponseEntity<StationsWrapperDto> resp = restTemplate.getForEntity(stationsApiUrl, StationsWrapperDto.class);
        if ( resp != null && resp.getStatusCode() == HttpStatus.OK) {
            StationsWrapperDto wrapperDto = resp.getBody();
            if ( wrapperDto != null && wrapperDto.getStations() != null ) {
                List<StationsDto> stations = new ArrayList<>();
                for (StationEntryDto e : wrapperDto.getStations()) {
                    StationsDto s = new StationsDto();
                    s.setStationName(e.getName());
                    s.setAbbreviation(e.getAbbr());
                    stations.add(s);
                }

                return stations;
            }
        }
        return new ArrayList<>();
    }


    public EstimationDto getEstimationForStation(String station) {
        String url = estimationApiUrl.replace("**ORIG**", station);
        ResponseEntity<EstimationWrapperDto> resp = restTemplate.getForEntity(url, EstimationWrapperDto.class);

        if ( resp != null && resp.getStatusCode() == HttpStatus.OK) {
            EstimationWrapperDto w = resp.getBody();
            if ( w != null && w.getStation() != null ) {
                StationWithEstimateEntryDto swe = w.getStation();
                EstimationDto estimate = new EstimationDto();
                estimate.setOriginStation(swe.getName());
                Map<String, Set<EstimationEntryDto>> estimateEntriesMap = new HashMap<>();
                if ( swe.getEtd() != null ) {
                    for (DestinationEstimateWrapperDto dw : swe.getEtd()) {
                        if (!estimateEntriesMap.containsKey(dw.getDestination())) {
                            estimateEntriesMap.put(dw.getDestination(), new TreeSet<EstimationEntryDto>());
                        }
                        if ( dw.getEstimate() != null ) {
                            for (com.mark.nevexandbart.model.bartapi.EstimationDto e : dw.getEstimate()) {
                                EstimationEntryDto entry = new EstimationEntryDto();
                                entry.setDirection(e.getDirection());
                                entry.setMinutes(e.getMinutes());
                                entry.setTrainName(dw.getDestination());
                                entry.setHexColor(e.getHexColor());
                                estimateEntriesMap.get(dw.getDestination()).add(entry);
                            }
                        }
                    }
                }

                Set<EstimationEntryDto> finalEntries = new TreeSet<>();
                for ( Set<EstimationEntryDto> es : estimateEntriesMap.values()) {
                    for ( EstimationEntryDto e : es) {
                        finalEntries.add(e);
                    }
                }
                estimate.setEntries(finalEntries);
                return estimate;
            }

        }
        return new EstimationDto();
    }
}
