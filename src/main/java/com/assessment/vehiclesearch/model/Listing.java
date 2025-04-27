package com.assessment.vehiclesearch.model;

import lombok.Data;

@Data
public class Listing {
    private String id;
    private int length;
    private int width;
    private String location_id;
    private int price_in_cents;

    public Listing(String id, int length, int width, String location_id, int price_in_cents) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.location_id = location_id;
        this.price_in_cents = price_in_cents;
    }
}