package com.mark.nevexandbart.ws;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by NeVeX on 9/5/2016.
 */
@Controller
@RequestMapping
class BartViewEndpoint {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome() {
        return "bart.html";
    }

}
