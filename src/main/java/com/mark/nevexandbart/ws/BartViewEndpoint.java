package com.mark.nevexandbart.ws;

import com.mark.nevexandbart.model.StationsDto;
import com.mark.nevexandbart.service.BartApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by NeVeX on 9/5/2016.
 */
@Controller
@RequestMapping
public class BartViewEndpoint {

    private final BartApiService bartApiService;

    @Autowired
    public BartViewEndpoint(BartApiService bartApiService) {
        this.bartApiService = bartApiService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome(ModelMap model) {

        List<StationsDto> stations = this.bartApiService.getStations();

        if ( stations != null ) {
            model.addAttribute("all_stations", stations);
        }
        return "welcome";
    }


    @RequestMapping(value = "/estimates", method = RequestMethod.GET)
    public String getEstimatesWithFragments(HttpServletRequest request, ModelMap map) {
        String station = request.getParameter("station");
        map.addAttribute("estimates", this.bartApiService.getEstimationForStation(station));
        return "bart_estimates :: estimates";
    }


}
