package com.assessment.vehiclesearch.model;

import lombok.Data;
import java.util.List;

@Data
public class VehicleSearchResult {
    private String location_id;
    private List<String> listing_ids;
    private int total_price_in_cents;
}
