package com.assessment.vehiclesearch.controller;

import com.assessment.vehiclesearch.model.VehicleSearchRequest;
import com.assessment.vehiclesearch.model.VehicleSearchResult;
import com.assessment.vehiclesearch.service.VehicleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/") // Base path
public class VehicleSearchController {

    @Autowired
    private VehicleSearchService searchService;

    @PostMapping
    public List<VehicleSearchResult> searchForVehicles(@RequestBody List<VehicleSearchRequest> vehicles) {
        return searchService.findMatchingListings(vehicles);
    }
}
