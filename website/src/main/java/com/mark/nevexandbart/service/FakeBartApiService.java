package com.mark.nevexandbart.service;

import com.mark.nevexandbart.config.BartProperties;
import com.mark.nevexandbart.model.EstimationDto;
import com.mark.nevexandbart.model.EstimationEntryDto;
import com.mark.nevexandbart.model.StationsDto;
import com.mark.nevexandbart.model.bartapi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by NeVeX on 9/5/2016.
 * <br>This class will act as a simple faker for testing
 */
@Service
@Profile("fake-api-service")
class FakeBartApiService implements BartApiService {

    final List<StationsDto> stations;

    FakeBartApiService() {
        stations = new ArrayList<>();
        stations.add(new StationsDto("Ashby", "ASH"));
        stations.add(new StationsDto("12th Street", "12TH"));
        stations.add(new StationsDto("Dublin", "DUB"));
    }

    @Override
    public List<StationsDto> getStations() {
        return stations;
    }

    @Override
    public EstimationDto getEstimationForStation(String station) {
        // lets' fake it
        EstimationDto estimate = new EstimationDto();
        estimate.setOriginStation(station);

        Set<EstimationEntryDto> entries = new HashSet<>();

        for ( int i = 0; i < 10; i++) {
            EstimationEntryDto e = new EstimationEntryDto();
            e.setHexColor("#49ed95");
            e.setTrainName("Beer Train");
            e.setMinutes(i);
            e.setDirection("North");
            entries.add(e);
        }
        estimate.setEntries(entries);
        return estimate;

    }
}
