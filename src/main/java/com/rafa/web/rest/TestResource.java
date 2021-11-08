package com.rafa.web.rest;

import com.rafa.web.base.AbstractBaseResource;
import com.rafa.web.base.ServiceResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestResource extends AbstractBaseResource {

    @RequestMapping(value = "/{msg}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ServiceResponse<String> hello(@PathVariable(name = "msg") String msg) {
        
        log.info("Received a GET /api/test/{} service call", msg);

        String now = System.currentTimeMillis()+"";
        return success(msg + " " + now);
    }
}
