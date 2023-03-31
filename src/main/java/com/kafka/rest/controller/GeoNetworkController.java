package com.kafka.rest.controller;

import com.kafka.core.models.GeoNetModel;
import com.kafka.core.services.IGeoNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geoNet")
public class GeoNetworkController {

    @Autowired
    private IGeoNetService iGeoNetService;


    @PostMapping("/send")
    public ResponseEntity<?>testEndpoint(@RequestBody GeoNetModel geoNetModel){
        return ResponseEntity.ok(iGeoNetService.test(geoNetModel));
    }
}
