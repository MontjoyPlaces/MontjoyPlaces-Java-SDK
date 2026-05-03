package com.montjoy.places;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public final class MontjoyPlaces {
    private final HttpClient httpClient;
    private final String apiKey;
    private final String baseUrl;

    public MontjoyPlaces(String apiKey) {
        this(apiKey, "https://api.montjoyplaces.com", HttpClient.newHttpClient());
    }

    public MontjoyPlaces(String apiKey, String baseUrl) {
        this(apiKey, baseUrl, HttpClient.newHttpClient());
    }

    public MontjoyPlaces(String apiKey, String baseUrl, HttpClient httpClient) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("apiKey is required");
        }
        this.apiKey = apiKey;
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.httpClient = httpClient;
    }

    public Models.BillingPlansResponse listBillingPlans() {
        return Mappers.billingPlans(send("GET", "/billing/plans", null, null));
    }

    public Models.WhoAmIResponse whoAmI() {
        return Mappers.whoAmI(send("GET", "/v1/whoami", null, null));
    }

    public Models.GroupsListResponse listGroups(Models.ListGroupsRequest request) {
        return Mappers.groupsList(send("GET", "/v1/groups", request, null));
    }

    public Models.GroupSingleResponse createGroup(Models.GroupCreateRequest request) {
        return Mappers.groupSingle(send("POST", "/v1/groups", null, request));
    }

    public Models.GroupSingleResponse updateGroup(String groupId, Models.GroupUpdateRequest request) {
        return Mappers.groupSingle(send("PUT", "/v1/groups/" + encode(groupId), null, request));
    }

    public Models.GroupDeleteResponse deleteGroup(String groupId) {
        return Mappers.groupDelete(send("DELETE", "/v1/groups/" + encode(groupId), null, null));
    }

    public Models.CustomPlacesListResponse listCustomPlaces(Models.ListCustomPlacesRequest request) {
        return Mappers.customPlacesList(send("GET", "/v1/custom-places", request, null));
    }

    public Models.CustomPlacesExportResponse exportCustomPlaces(Models.ExportCustomPlacesRequest request) {
        return Mappers.customPlacesExport(send("GET", "/v1/custom-places/export", request, null));
    }

    public Models.CustomPlacesImportResponse importCustomPlaces(Models.CustomPlacesImportRequest request) {
        return Mappers.customPlacesImport(send("POST", "/v1/custom-places/import", null, request));
    }

    public Models.CustomPlaceSingleResponse createCustomPlace(Models.CustomPlaceCreateRequest request) {
        return Mappers.customPlaceSingle(send("POST", "/v1/custom-places", null, request));
    }

    public Models.CustomPlaceSingleResponse getCustomPlace(String customPlaceId) {
        return Mappers.customPlaceSingle(send("GET", "/v1/custom-places/" + encode(customPlaceId), null, null));
    }

    public Models.CustomPlaceSingleResponse updateCustomPlace(String customPlaceId, Models.CustomPlaceUpdateRequest request) {
        return Mappers.customPlaceSingle(send("PUT", "/v1/custom-places/" + encode(customPlaceId), null, request));
    }

    public Models.DeleteResponse deleteCustomPlace(String customPlaceId) {
        return Mappers.deleteResponse(send("DELETE", "/v1/custom-places/" + encode(customPlaceId), null, null));
    }

    public Models.CustomPlaceSingleResponse hideCustomPlace(String customPlaceId, Models.CustomPlaceHideRequest request) {
        return Mappers.customPlaceSingle(send("POST", "/v1/custom-places/" + encode(customPlaceId) + "/hide", null, request));
    }

    public Models.PlaceSingleResponse getPlace(String placeId) {
        return Mappers.placeSingleResponse(send("GET", "/v1/places/" + encode(placeId), null, null));
    }

    public Models.OverrideResponse overridePlace(String fsqPlaceId, Models.OverrideRequest request) {
        return Mappers.overrideResponse(send("PUT", "/v1/places/" + encode(fsqPlaceId) + "/override", null, request));
    }

    public Models.UsCityListResponse lookupNearestUsCities(Models.NearestUsCitiesRequest request) {
        return Mappers.usCityList(send("GET", "/v1/lookup/us-cities/nearest", request, null));
    }

    public Models.UsCitySearchResponse searchUsCities(Models.SearchUsCitiesRequest request) {
        return Mappers.usCitySearch(send("GET", "/v1/lookup/us-cities/search", request, null));
    }

    public Models.UsZipLookupResponse lookupUsZipcode(String zipcode) {
        return Mappers.usZipLookup(send("GET", "/v1/lookup/us-cities/zip/" + encode(zipcode), null, null));
    }

    public Models.CategorySearchResponse searchCategories(Models.SearchCategoriesRequest request) {
        return Mappers.categorySearch(send("GET", "/v1/lookup/categories/search", request, null));
    }

    public Models.CategoryResponse getCategory(String categoryId) {
        return Mappers.categoryResponse(send("GET", "/v1/lookup/categories/" + encode(categoryId), null, null));
    }

    public Models.CategoryChildrenResponse getCategoryChildren(String categoryId, Models.CategoryChildrenRequest request) {
        return Mappers.categoryChildren(send("GET", "/v1/lookup/categories/" + encode(categoryId) + "/children", request, null));
    }

    public Models.SearchResponse searchPlaces(Models.SearchPlacesRequest request) {
        return Mappers.searchResponse(send("GET", "/v1/search", request, null));
    }

    private Object send(String method, String path, Object query, Object body) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(buildUri(path, query))
                    .header("Accept", "application/json")
                    .header("X-API-Key", apiKey);

            if (body != null) {
                builder.header("Content-Type", "application/json");
                builder.method(method, HttpRequest.BodyPublishers.ofString(Json.stringify(Mappers.toBody(body))));
            } else {
                builder.method(method, HttpRequest.BodyPublishers.noBody());
            }

            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            Object payload = response.body() == null || response.body().isBlank() ? null : Json.parse(response.body());
            if (response.statusCode() >= 400) {
                String message = payload instanceof Map<?, ?> map && map.get("error") != null
                        ? String.valueOf(map.get("error"))
                        : "Request failed with status " + response.statusCode();
                throw new MontjoyPlacesException(message, response.statusCode(), payload);
            }
            return payload;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to call Montjoy Places API", exception);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to call Montjoy Places API", exception);
        }
    }

    private URI buildUri(String path, Object query) {
        StringBuilder builder = new StringBuilder(baseUrl).append(path);
        Map<String, String> queryParameters = Mappers.toQuery(query);
        if (!queryParameters.isEmpty()) {
            StringJoiner joiner = new StringJoiner("&");
            for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                joiner.add(encode(entry.getKey()) + "=" + encode(entry.getValue()));
            }
            builder.append('?').append(joiner);
        }
        return URI.create(builder.toString());
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
