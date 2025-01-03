package com.example.BE_Heatmap.controller;

import com.example.BE_Heatmap.dto.LocationDto;
import com.example.BE_Heatmap.dto.LocationFilterRequest;
import com.example.BE_Heatmap.dto.LocationRequest;
import com.example.BE_Heatmap.dto.LocationResponse;
import com.example.BE_Heatmap.security.JwtUtil;
import com.example.BE_Heatmap.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public LocationController(LocationService locationService, JwtUtil jwtUtil) {
        this.locationService = locationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/saveLocations")
    public ResponseEntity<LocationResponse> saveLocations(@RequestHeader("Authorization") String bearerToken, @RequestBody LocationRequest locationData) {
        try {
            String token = bearerToken.split(" ")[1];
            String userId = jwtUtil.extractUserId(token);
            List<LocationDto> locations = locationService.saveAll(locationData.getData(), userId);
            LocationResponse locationResponse = new LocationResponse(locations, "Success");
            return new ResponseEntity<>(locationResponse, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            LocationResponse locationResponse = new LocationResponse(List.of(), "Failure", e.getMessage(), "");
            return new ResponseEntity<>(locationResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getLocations")
    public ResponseEntity<LocationResponse> getLocations(@RequestHeader("Authorization") String bearerToken, @RequestBody LocationFilterRequest locationFilter) {
        try {
            String token = bearerToken.split(" ")[1];
            String userId = jwtUtil.extractUserId(token);
            List<LocationDto> locations;
            if(locationFilter.getFilter().getStartDate() != null && locationFilter.getFilter().getEndDate() != null) {
                locations = locationService.getLocationWithinStartNEndDate(userId, locationFilter.getFilter());
            }else {
                locations = locationService.getAllLocation(userId);
            }
            LocationResponse locationResponse = new LocationResponse(locations, "Success");
            return new ResponseEntity<>(locationResponse, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            LocationResponse locationResponse = new LocationResponse(List.of(), "Failure", e.getMessage(), "");
            return new ResponseEntity<>(locationResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
