package com.assessment.vehiclesearch;

import com.assessment.vehiclesearch.model.VehicleSearchRequest;
import com.assessment.vehiclesearch.model.VehicleSearchResult;
import com.assessment.vehiclesearch.service.VehicleSearchService;
import com.assessment.vehiclesearch.model.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SearchServiceTest {

	private VehicleSearchService searchService;

	@BeforeEach
	void setUp() {
		searchService = new VehicleSearchService();

		// Mock the listings.
		List<Listing> mockListings = new ArrayList<>();
		mockListings.add(new Listing("id1", 20, 20, "loc1", 100));
		mockListings.add(new Listing("id2", 20, 20, "loc1", 90));
		mockListings.add(new Listing("id3", 10, 20, "loc1", 150));
		mockListings.add(new Listing("id4", 45, 20, "loc2", 100));
		mockListings.add(new Listing("id5", 25, 20, "loc2", 100));
		mockListings.add(new Listing("id6", 30, 20, "loc2", 80));
		mockListings.add(new Listing("id7", 10, 20, "loc3", 90));
		mockListings.add(new Listing("id8", 30, 20, "loc3", 150));

		searchService.setListings(mockListings);
	}

	@Test
	void testFindMatchingListingsOneRequest_success() {
		// Arrange.
		List<VehicleSearchRequest> vehicleRequests = new ArrayList<>();
		vehicleRequests.add(new VehicleSearchRequest(20, 1));

		// Act.
		List<VehicleSearchResult> results = searchService.findMatchingListings(vehicleRequests);

		// Assert.
		assertFalse(results.isEmpty());
		assertEquals(3, results.size());

		VehicleSearchResult result = results.get(0);
		assertNotNull(result.getLocation_id());
		assertNotNull(result.getListing_ids());
		assertEquals("loc2", result.getLocation_id());
		assertEquals(80, result.getTotal_price_in_cents());

	}

	@Test
	void testFindMatchingListingsTwoRequest_success() {
		// Arrange.
		List<VehicleSearchRequest> vehicleRequests = new ArrayList<>();
		vehicleRequests.add(new VehicleSearchRequest(10, 1));
		vehicleRequests.add(new VehicleSearchRequest(20, 2));

		// Act.
		List<VehicleSearchResult> results = searchService.findMatchingListings(vehicleRequests);

		// Assert.
		assertFalse(results.isEmpty());
		assertEquals(2, results.size());

		VehicleSearchResult result = results.get(0);
		assertNotNull(result.getLocation_id());
		assertNotNull(result.getListing_ids());
		assertEquals("loc2", result.getLocation_id());
		assertEquals(280, result.getTotal_price_in_cents());

	}

	@Test
	void testFindMatchingListings_noMatch() {
		List<VehicleSearchRequest> vehicleRequests = new ArrayList<>();
		vehicleRequests.add(new VehicleSearchRequest(50, 1)); // No listing big enough

		List<VehicleSearchResult> results = searchService.findMatchingListings(vehicleRequests);

		assertNotNull(results);
		assertTrue(results.isEmpty()); // No matching location
	}
}
