package com.assessment.vehiclesearch.service;

import com.assessment.vehiclesearch.model.VehicleSearchRequest;
import com.assessment.vehiclesearch.model.VehicleSearchResult;
import com.assessment.vehiclesearch.model.Listing;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Service
public class VehicleSearchService {
    private List<Listing> listings;
    private static int width = 10;

    @Data
    static class MatchResult {
        private boolean success;
        private List<Listing> matchedListings;

        public MatchResult(boolean success, List<Listing> matchedListings) {
            this.success = success;
            this.matchedListings = matchedListings;
        }
    }

    @PostConstruct
    public void init() {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            InputStream stream = getClass().getClassLoader().getResourceAsStream("listings.json");
            listings = mapper.readValue(stream, new TypeReference<List<Listing>>() {
            });
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to load listings.json", e);
        }
    }

    /**
     * The results should:
     * 1. Include every possible location that could store all requested vehicles
     * 1. Include the cheapest possible combination of listings per location
     * 1. Include only one result per location_id
     * 1. Be sorted by the total price in cents, ascending
     * <p>
     * Give a list of VehicleSearchRequest,
     * 1. Group by location_id
     * 2. For each location_id and its corresponding listings, determine if this location_id can store all requested vehicles.
     * 3. Get all location_id can store all requested vehicles.
     */
    public List<VehicleSearchResult> findMatchingListings(List<VehicleSearchRequest> vehicleRequests) {
        // Group listing by location_id.
        Map<String, List<Listing>> locationGroupedListings = GetListingsByLocation(listings);
        List<VehicleSearchResult> results = new ArrayList<>();
        for (String locationId : locationGroupedListings.keySet()) {
            List<Listing> listings = locationGroupedListings.get(locationId);
            MatchResult matchResult = GetAllUsedListings(listings, vehicleRequests);
            if (!matchResult.success)
            {
               continue;
            }

            // Find a location can fit all vehicles and all listings used to stored vehicles.
            VehicleSearchResult result = new VehicleSearchResult();
            result.setLocation_id(locationId);
            result.setListing_ids(matchResult.getMatchedListings().stream().map(Listing::getId).collect(Collectors.toList()));
            int totalPrice = matchResult.getMatchedListings().stream().mapToInt(Listing::getPrice_in_cents).sum();
            result.setTotal_price_in_cents(totalPrice);
            results.add(result);
        }

        results.sort(Comparator.comparingInt(VehicleSearchResult::getTotal_price_in_cents));
        return results;
    }

    /*
    * Group listing by their location_id.
    * */
    private Map<String, List<Listing>> GetListingsByLocation(List<Listing> listings) {
        Map<String, List<Listing>> listingsByLocation = new HashMap<>();
        for (Listing listing : listings) {
            String locationId = listing.getLocation_id();
            listingsByLocation.putIfAbsent(locationId, new ArrayList<>());
            listingsByLocation.get(locationId).add(listing);
        }

        return listingsByLocation;
    }

   /*
   * Find all used listing.
   * Return a tuple of whether the given listings can fit all requests and all the listing being used.
   * */
    private MatchResult GetAllUsedListings(List<Listing> listings, List<VehicleSearchRequest> vehicleRequests) {
        // Sort listings by cheapest first, then by length. so always use cheapest + shortest.
        listings.sort(Comparator
                .comparingInt(Listing::getPrice_in_cents) // Price ascending.
                .thenComparing(Comparator.comparingInt(Listing::getLength).reversed())); // Length descending.

        vehicleRequests.sort(Comparator.comparingInt(VehicleSearchRequest::getLength).reversed());
        Set<Listing> usedListings = new HashSet<>();
        int vehicleIndex = 0;
        while (vehicleIndex < vehicleRequests.size()) {
            VehicleSearchRequest request = vehicleRequests.get(vehicleIndex);
            int toMatchCount = request.getQuantity();
            for (Listing listing : listings) {
                if (usedListings.contains(listing)) {
                    continue;
                }
                if (listing.getWidth() >= width && listing.getLength() >= request.getLength()) {
                    toMatchCount--;
                    usedListings.add(listing);
                }
                if (toMatchCount == 0) {
                    break;
                }
            }
            if (toMatchCount > 0) { // No fit for this vehicle.
                return new MatchResult(false, new ArrayList<>());
            }
            vehicleIndex++;
        }
        return new MatchResult(true, new ArrayList<>(usedListings));
    }
}
