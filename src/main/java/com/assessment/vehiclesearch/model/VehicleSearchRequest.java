package com.assessment.vehiclesearch.model;

import lombok.Data;

@Data
public class VehicleSearchRequest {
    private int length;
    private int quantity;

    public VehicleSearchRequest(int length, int quantity) {
        this.length = length;
        this.quantity = quantity;
    }
}
