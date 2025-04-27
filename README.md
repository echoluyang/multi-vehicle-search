# Multi-Vehicle Search API

## Overview

This project provides a search API that allows users to find storage locations where multiple vehicles can be accommodated.  
The solution is designed to find the cheapest combination of listings per location, ensuring that all requested vehicles fit based on their dimensions (width is fixed as 10).

The API accepts a list of vehicles (length and quantity) and returns all locations can fit all vehicles, sorted by total price ascending.

---

## Deployment

The API is deployed on [Render](https://multi-vehicle-search-n3ow.onrender.com) using the free service plan.

**Note:**  
The free plan may cause the service to go idle when there’s no traffic.  
As a result:
- The **first request** may take **over a minute** to respond due to cold start.
- Subsequent requests should have **normal performance**.

---

## Areas for Further Improvement
There are several areas identified for further enhancement:
-Adding authentication to the API.
-Expanding the coverage and robustness of unit tests.
-Adding more detailed comments throughout the codebase for better clarity.
-Implementing more comprehensive error handling for invalid or malformed requests.

Note:
Due to time constraints, these improvements were not fully developed in this version.
