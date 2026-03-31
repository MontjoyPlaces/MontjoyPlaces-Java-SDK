package com.montjoy.places;

import java.time.OffsetDateTime;
import java.util.List;

public final class Models {
    private Models() {
    }

    public record ErrorResponse(String error) {}

    public record PlanCatalogEntry(
            String code,
            String label,
            Integer monthlyRequests,
            Integer maxTenants,
            Integer maxApps,
            Integer maxApiKeys,
            boolean overageAllowed,
            int overageBlockRequests,
            int overageBlockPriceCents,
            Integer maxUsageMultiplier,
            boolean hardCapByDefault) {}

    public record BillingPlansResponse(boolean ok, List<PlanCatalogEntry> plans) {}

    public record WhoAmIResponse(boolean ok, String apiKeyId, String tenantId, String appId, String keyName, String prefix) {}

    public record Place(
            String fsqPlaceId,
            String placeSource,
            String name,
            double latitude,
            double longitude,
            String address,
            String locality,
            String region,
            String postcode,
            String country,
            String website,
            String tel,
            String email,
            String formattedAddress,
            String geocodeProvider,
            Double geocodeConfidence,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {}

    public record PlaceSingleResponse(boolean ok, Place row) {}

    public record Group(String groupId, String tenantId, String name, OffsetDateTime createdAt) {}

    public record GroupsListResponse(boolean ok, List<Group> rows) {}

    public record GroupSingleResponse(boolean ok, Group row) {}

    public record GroupDeleteResponse(boolean ok, boolean deleted) {}

    public record DeleteResponse(boolean ok, Boolean deleted) {}

    public record CustomPlace(
            String customPlaceId,
            String tenantId,
            String appId,
            String groupId,
            String ownerUserId,
            String source,
            String fsqPlaceId,
            String name,
            double latitude,
            double longitude,
            String address,
            String locality,
            String region,
            String postcode,
            String country,
            String website,
            String tel,
            String email,
            Object tags,
            Object meta,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            Double distMeters) {}

    public record CustomPlacesListResponse(boolean ok, List<CustomPlace> rows, String nextCursor) {}

    public record CustomPlaceSingleResponse(boolean ok, CustomPlace row) {}

    public sealed interface SearchRow permits SearchRowGlobal, SearchRowCustom {
        String source();
    }

    public record SearchRowGlobal(
            String fsqPlaceId,
            String name,
            double latitude,
            double longitude,
            double distMeters,
            String categoryName,
            String source) implements SearchRow {}

    public record SearchRowCustom(CustomPlace place, String source) implements SearchRow {}

    public record SearchResolvedCenter(double lat, double lon, String source, String kind, String label) {}

    public record SearchResolved(
            String mode,
            String reason,
            String prefix,
            String categoryName,
            String groupId,
            Boolean customOnly,
            String localityText,
            String addressQuery,
            String addressPlaceId,
            String formattedAddress,
            String geocodeProvider,
            Boolean geocodeCacheHit,
            Double addressRadiusMeters,
            Integer addressCandidateCount,
            Integer addressFilteredCount,
            SearchResolvedCenter center) {}

    public record SearchResponse(boolean ok, String mode, String q, SearchResolved resolved, int count, List<SearchRow> rows) {}

    public record UsCity(int id, String city, String stateId, String stateName, String zipcode, double lat, double lon, Double distMeters) {}

    public record UsCityListResponse(boolean ok, int count, List<UsCity> rows) {}

    public record UsCitySearchResponse(boolean ok, String q, String state, int count, List<UsCity> rows) {}

    public record UsZipLookupResponse(boolean ok, String zipcode, int count, List<UsCity> rows) {}

    public record CategoryHierarchyNode(int level, String categoryId, String categoryName) {}

    public record CategoryLookupRow(String categoryId, String categoryName, String categoryLabel, Integer categoryLevel, List<CategoryHierarchyNode> hierarchy) {}

    public record CategorySearchResponse(boolean ok, String q, Integer level, String parentId, int count, List<CategoryLookupRow> rows) {}

    public record CategoryResponse(boolean ok, CategoryLookupRow row) {}

    public record CategoryChildrenResponse(boolean ok, CategoryLookupRow parent, int count, List<CategoryLookupRow> rows) {}

    public record OverrideResponse(boolean ok, String action, CustomPlace row) {}

    public static final class GroupCreateRequest {
        private final String name;

        public GroupCreateRequest(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static final class GroupUpdateRequest {
        private final String name;

        public GroupUpdateRequest(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static final class ListGroupsRequest {
        private Integer limit;

        public Integer getLimit() {
            return limit;
        }

        public ListGroupsRequest limit(Integer limit) {
            this.limit = limit;
            return this;
        }
    }

    public static final class CustomPlaceCreateRequest {
        private String groupId;
        private String source;
        private String ownerUserId;
        private String fsqPlaceId;
        private final String name;
        private final double latitude;
        private final double longitude;
        private String address;
        private String locality;
        private String region;
        private String postcode;
        private String country;
        private String website;
        private String tel;
        private String email;
        private Object tags;
        private Object meta;

        public CustomPlaceCreateRequest(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getGroupId() { return groupId; }
        public String getSource() { return source; }
        public String getOwnerUserId() { return ownerUserId; }
        public String getFsqPlaceId() { return fsqPlaceId; }
        public String getName() { return name; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public String getAddress() { return address; }
        public String getLocality() { return locality; }
        public String getRegion() { return region; }
        public String getPostcode() { return postcode; }
        public String getCountry() { return country; }
        public String getWebsite() { return website; }
        public String getTel() { return tel; }
        public String getEmail() { return email; }
        public Object getTags() { return tags; }
        public Object getMeta() { return meta; }

        public CustomPlaceCreateRequest groupId(String value) { this.groupId = value; return this; }
        public CustomPlaceCreateRequest source(String value) { this.source = value; return this; }
        public CustomPlaceCreateRequest ownerUserId(String value) { this.ownerUserId = value; return this; }
        public CustomPlaceCreateRequest fsqPlaceId(String value) { this.fsqPlaceId = value; return this; }
        public CustomPlaceCreateRequest address(String value) { this.address = value; return this; }
        public CustomPlaceCreateRequest locality(String value) { this.locality = value; return this; }
        public CustomPlaceCreateRequest region(String value) { this.region = value; return this; }
        public CustomPlaceCreateRequest postcode(String value) { this.postcode = value; return this; }
        public CustomPlaceCreateRequest country(String value) { this.country = value; return this; }
        public CustomPlaceCreateRequest website(String value) { this.website = value; return this; }
        public CustomPlaceCreateRequest tel(String value) { this.tel = value; return this; }
        public CustomPlaceCreateRequest email(String value) { this.email = value; return this; }
        public CustomPlaceCreateRequest tags(Object value) { this.tags = value; return this; }
        public CustomPlaceCreateRequest meta(Object value) { this.meta = value; return this; }
    }

    public static final class CustomPlaceUpdateRequest {
        private String name;
        private Double latitude;
        private Double longitude;
        private String address;
        private String locality;
        private String region;
        private String postcode;
        private String country;
        private String website;
        private String tel;
        private String email;
        private Object tags;
        private Object meta;

        public String getName() { return name; }
        public Double getLatitude() { return latitude; }
        public Double getLongitude() { return longitude; }
        public String getAddress() { return address; }
        public String getLocality() { return locality; }
        public String getRegion() { return region; }
        public String getPostcode() { return postcode; }
        public String getCountry() { return country; }
        public String getWebsite() { return website; }
        public String getTel() { return tel; }
        public String getEmail() { return email; }
        public Object getTags() { return tags; }
        public Object getMeta() { return meta; }

        public CustomPlaceUpdateRequest name(String value) { this.name = value; return this; }
        public CustomPlaceUpdateRequest latitude(Double value) { this.latitude = value; return this; }
        public CustomPlaceUpdateRequest longitude(Double value) { this.longitude = value; return this; }
        public CustomPlaceUpdateRequest address(String value) { this.address = value; return this; }
        public CustomPlaceUpdateRequest locality(String value) { this.locality = value; return this; }
        public CustomPlaceUpdateRequest region(String value) { this.region = value; return this; }
        public CustomPlaceUpdateRequest postcode(String value) { this.postcode = value; return this; }
        public CustomPlaceUpdateRequest country(String value) { this.country = value; return this; }
        public CustomPlaceUpdateRequest website(String value) { this.website = value; return this; }
        public CustomPlaceUpdateRequest tel(String value) { this.tel = value; return this; }
        public CustomPlaceUpdateRequest email(String value) { this.email = value; return this; }
        public CustomPlaceUpdateRequest tags(Object value) { this.tags = value; return this; }
        public CustomPlaceUpdateRequest meta(Object value) { this.meta = value; return this; }
    }

    public static final class CustomPlaceHideRequest {
        private final boolean hidden;

        public CustomPlaceHideRequest(boolean hidden) {
            this.hidden = hidden;
        }

        public boolean isHidden() {
            return hidden;
        }
    }

    public static final class ListCustomPlacesRequest {
        private String groupId;
        private Integer limit;
        private String cursor;
        private Boolean includeHidden;

        public String getGroupId() { return groupId; }
        public Integer getLimit() { return limit; }
        public String getCursor() { return cursor; }
        public Boolean getIncludeHidden() { return includeHidden; }

        public ListCustomPlacesRequest groupId(String value) { this.groupId = value; return this; }
        public ListCustomPlacesRequest limit(Integer value) { this.limit = value; return this; }
        public ListCustomPlacesRequest cursor(String value) { this.cursor = value; return this; }
        public ListCustomPlacesRequest includeHidden(Boolean value) { this.includeHidden = value; return this; }
    }

    public static final class OverrideRequest {
        private String groupId;
        private Boolean hide;
        private String name;
        private Double latitude;
        private Double longitude;
        private String address;
        private String locality;
        private String region;
        private String postcode;
        private String country;
        private String website;
        private String tel;
        private String email;
        private Object tags;
        private Object meta;

        public String getGroupId() { return groupId; }
        public Boolean getHide() { return hide; }
        public String getName() { return name; }
        public Double getLatitude() { return latitude; }
        public Double getLongitude() { return longitude; }
        public String getAddress() { return address; }
        public String getLocality() { return locality; }
        public String getRegion() { return region; }
        public String getPostcode() { return postcode; }
        public String getCountry() { return country; }
        public String getWebsite() { return website; }
        public String getTel() { return tel; }
        public String getEmail() { return email; }
        public Object getTags() { return tags; }
        public Object getMeta() { return meta; }

        public OverrideRequest groupId(String value) { this.groupId = value; return this; }
        public OverrideRequest hide(Boolean value) { this.hide = value; return this; }
        public OverrideRequest name(String value) { this.name = value; return this; }
        public OverrideRequest latitude(Double value) { this.latitude = value; return this; }
        public OverrideRequest longitude(Double value) { this.longitude = value; return this; }
        public OverrideRequest address(String value) { this.address = value; return this; }
        public OverrideRequest locality(String value) { this.locality = value; return this; }
        public OverrideRequest region(String value) { this.region = value; return this; }
        public OverrideRequest postcode(String value) { this.postcode = value; return this; }
        public OverrideRequest country(String value) { this.country = value; return this; }
        public OverrideRequest website(String value) { this.website = value; return this; }
        public OverrideRequest tel(String value) { this.tel = value; return this; }
        public OverrideRequest email(String value) { this.email = value; return this; }
        public OverrideRequest tags(Object value) { this.tags = value; return this; }
        public OverrideRequest meta(Object value) { this.meta = value; return this; }
    }

    public static final class NearestUsCitiesRequest {
        private final double lat;
        private final double lon;
        private Integer limit;

        public NearestUsCitiesRequest(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() { return lat; }
        public double getLon() { return lon; }
        public Integer getLimit() { return limit; }
        public NearestUsCitiesRequest limit(Integer value) { this.limit = value; return this; }
    }

    public static final class SearchUsCitiesRequest {
        private final String q;
        private String state;
        private Integer limit;

        public SearchUsCitiesRequest(String q) {
            this.q = q;
        }

        public String getQ() { return q; }
        public String getState() { return state; }
        public Integer getLimit() { return limit; }
        public SearchUsCitiesRequest state(String value) { this.state = value; return this; }
        public SearchUsCitiesRequest limit(Integer value) { this.limit = value; return this; }
    }

    public static final class SearchCategoriesRequest {
        private String q;
        private Integer level;
        private String parentId;
        private Integer limit;

        public String getQ() { return q; }
        public Integer getLevel() { return level; }
        public String getParentId() { return parentId; }
        public Integer getLimit() { return limit; }
        public SearchCategoriesRequest q(String value) { this.q = value; return this; }
        public SearchCategoriesRequest level(Integer value) { this.level = value; return this; }
        public SearchCategoriesRequest parentId(String value) { this.parentId = value; return this; }
        public SearchCategoriesRequest limit(Integer value) { this.limit = value; return this; }
    }

    public static final class CategoryChildrenRequest {
        private Integer limit;
        public Integer getLimit() { return limit; }
        public CategoryChildrenRequest limit(Integer value) { this.limit = value; return this; }
    }

    public static final class SearchPlacesRequest {
        private final String q;
        private Double lat;
        private Double lon;
        private Double radiusMeters;
        private Integer limit;
        private Boolean excludeCategoryMatch;
        private Boolean forceTypeahead;
        private Boolean customOnly;
        private Boolean onlyCustom;
        private Boolean isAddress;
        private String groupId;

        public SearchPlacesRequest(String q) {
            this.q = q;
        }

        public String getQ() { return q; }
        public Double getLat() { return lat; }
        public Double getLon() { return lon; }
        public Double getRadiusMeters() { return radiusMeters; }
        public Integer getLimit() { return limit; }
        public Boolean getExcludeCategoryMatch() { return excludeCategoryMatch; }
        public Boolean getForceTypeahead() { return forceTypeahead; }
        public Boolean getCustomOnly() { return customOnly; }
        public Boolean getOnlyCustom() { return onlyCustom; }
        public Boolean getIsAddress() { return isAddress; }
        public String getGroupId() { return groupId; }

        public SearchPlacesRequest lat(Double value) { this.lat = value; return this; }
        public SearchPlacesRequest lon(Double value) { this.lon = value; return this; }
        public SearchPlacesRequest radiusMeters(Double value) { this.radiusMeters = value; return this; }
        public SearchPlacesRequest limit(Integer value) { this.limit = value; return this; }
        public SearchPlacesRequest excludeCategoryMatch(Boolean value) { this.excludeCategoryMatch = value; return this; }
        public SearchPlacesRequest forceTypeahead(Boolean value) { this.forceTypeahead = value; return this; }
        public SearchPlacesRequest customOnly(Boolean value) { this.customOnly = value; return this; }
        public SearchPlacesRequest onlyCustom(Boolean value) { this.onlyCustom = value; return this; }
        public SearchPlacesRequest isAddress(Boolean value) { this.isAddress = value; return this; }
        public SearchPlacesRequest groupId(String value) { this.groupId = value; return this; }
    }
}
