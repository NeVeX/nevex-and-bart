package com.mark.nevexandbart.ws;

import com.mark.nevexandbart.model.StationsDto;
import com.mark.nevexandbart.service.BartApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeVeX on 9/5/2016.
 */
@RestController
@RequestMapping("/api")
public class BartApiEndpoint {

    private final BartApiService bartApiService;

    @Autowired
    public BartApiEndpoint(BartApiService bartApiService) {
        this.bartApiService = bartApiService;
    }

    @RequestMapping(path = "/stations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationsDto>> getAllStations() {
        List<StationsDto> stations = bartApiService.getStations();
        return ResponseEntity.ok(stations);

    }

    @RequestMapping(path = "/estimates", params = {"station"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllStations(HttpServletRequest request) {
        String station = request.getParameter("station");
        if (StringUtils.isEmpty(station)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(bartApiService.getEstimationForStation(station));
    }


}
