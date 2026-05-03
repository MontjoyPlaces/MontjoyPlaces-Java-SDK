package com.montjoy.places;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class Mappers {
    private Mappers() {
    }

    static Models.WhoAmIResponse whoAmI(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.WhoAmIResponse(bool(map, "ok"), string(map, "apiKeyId"), string(map, "tenantId"),
                string(map, "appId"), string(map, "keyName"), string(map, "prefix"), bool(map, "readOnly"));
    }

    static Models.BillingPlansResponse billingPlans(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.BillingPlansResponse(bool(map, "ok"), list(map, "plans").stream().map(Mappers::planCatalogEntry).toList());
    }

    static Models.GroupsListResponse groupsList(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.GroupsListResponse(bool(map, "ok"), list(map, "rows").stream().map(Mappers::group).toList());
    }

    static Models.GroupSingleResponse groupSingle(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.GroupSingleResponse(bool(map, "ok"), group(map.get("row")));
    }

    static Models.GroupDeleteResponse groupDelete(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.GroupDeleteResponse(bool(map, "ok"), bool(map, "deleted"));
    }

    static Models.DeleteResponse deleteResponse(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.DeleteResponse(bool(map, "ok"), nullableBoolean(map.get("deleted")));
    }

    static Models.CustomPlacesListResponse customPlacesList(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CustomPlacesListResponse(
                bool(map, "ok"),
                list(map, "rows").stream().map(Mappers::customPlace).toList(),
                nullableString(map.get("nextCursor")));
    }

    static Models.CustomPlacesExportResponse customPlacesExport(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CustomPlacesExportResponse(
                bool(map, "ok"),
                integer(map, "count"),
                list(map, "rows").stream().map(Mappers::customPlace).toList(),
                nullableString(map.get("nextCursor")));
    }

    static Models.CustomPlacesImportResponse customPlacesImport(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CustomPlacesImportResponse(
                bool(map, "ok"),
                integer(map, "imported"),
                integer(map, "created"),
                integer(map, "updated"),
                list(map, "rows").stream().map(Mappers::importedCustomPlace).toList());
    }

    static Models.CustomPlaceSingleResponse customPlaceSingle(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CustomPlaceSingleResponse(bool(map, "ok"), customPlace(map.get("row")));
    }

    static Models.PlaceSingleResponse placeSingleResponse(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.PlaceSingleResponse(bool(map, "ok"), map.get("row") == null ? null : place(map.get("row")));
    }

    static Models.OverrideResponse overrideResponse(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.OverrideResponse(bool(map, "ok"), string(map, "action"), customPlace(map.get("row")));
    }

    static Models.UsCityListResponse usCityList(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.UsCityListResponse(bool(map, "ok"), integer(map, "count"), list(map, "rows").stream().map(Mappers::usCity).toList());
    }

    static Models.UsCitySearchResponse usCitySearch(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.UsCitySearchResponse(bool(map, "ok"), string(map, "q"), nullableString(map.get("state")),
                integer(map, "count"), list(map, "rows").stream().map(Mappers::usCity).toList());
    }

    static Models.UsZipLookupResponse usZipLookup(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.UsZipLookupResponse(bool(map, "ok"), string(map, "zipcode"), integer(map, "count"),
                list(map, "rows").stream().map(Mappers::usCity).toList());
    }

    static Models.CategorySearchResponse categorySearch(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CategorySearchResponse(bool(map, "ok"), nullableString(map.get("q")), nullableInteger(map.get("level")),
                nullableString(map.get("parentId")), integer(map, "count"), list(map, "rows").stream().map(Mappers::category).toList());
    }

    static Models.CategoryResponse categoryResponse(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CategoryResponse(bool(map, "ok"), category(map.get("row")));
    }

    static Models.CategoryChildrenResponse categoryChildren(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CategoryChildrenResponse(bool(map, "ok"), category(map.get("parent")), integer(map, "count"),
                list(map, "rows").stream().map(Mappers::category).toList());
    }

    static Models.SearchResponse searchResponse(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.SearchResponse(bool(map, "ok"), string(map, "mode"), string(map, "q"), searchResolved(map.get("resolved")),
                integer(map, "count"), list(map, "rows").stream().map(Mappers::searchRow).toList());
    }

    static Models.ErrorResponse error(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.ErrorResponse(string(map, "error"));
    }

    static Map<String, Object> toBody(Object request) {
        Map<String, Object> body = new LinkedHashMap<>();
        if (request instanceof Models.GroupCreateRequest groupCreateRequest) {
            body.put("name", groupCreateRequest.getName());
        } else if (request instanceof Models.GroupUpdateRequest groupUpdateRequest) {
            body.put("name", groupUpdateRequest.getName());
        } else if (request instanceof Models.CustomPlaceCreateRequest customPlaceCreateRequest) {
            put(body, "groupId", customPlaceCreateRequest.getGroupId());
            put(body, "source", customPlaceCreateRequest.getSource());
            put(body, "ownerUserId", customPlaceCreateRequest.getOwnerUserId());
            put(body, "fsqPlaceId", customPlaceCreateRequest.getFsqPlaceId());
            body.put("name", customPlaceCreateRequest.getName());
            body.put("latitude", customPlaceCreateRequest.getLatitude());
            body.put("longitude", customPlaceCreateRequest.getLongitude());
            put(body, "address", customPlaceCreateRequest.getAddress());
            put(body, "locality", customPlaceCreateRequest.getLocality());
            put(body, "region", customPlaceCreateRequest.getRegion());
            put(body, "postcode", customPlaceCreateRequest.getPostcode());
            put(body, "country", customPlaceCreateRequest.getCountry());
            put(body, "website", customPlaceCreateRequest.getWebsite());
            put(body, "tel", customPlaceCreateRequest.getTel());
            put(body, "email", customPlaceCreateRequest.getEmail());
            put(body, "tags", customPlaceCreateRequest.getTags());
            put(body, "meta", customPlaceCreateRequest.getMeta());
        } else if (request instanceof Models.CustomPlaceUpdateRequest customPlaceUpdateRequest) {
            put(body, "name", customPlaceUpdateRequest.getName());
            put(body, "latitude", customPlaceUpdateRequest.getLatitude());
            put(body, "longitude", customPlaceUpdateRequest.getLongitude());
            put(body, "address", customPlaceUpdateRequest.getAddress());
            put(body, "locality", customPlaceUpdateRequest.getLocality());
            put(body, "region", customPlaceUpdateRequest.getRegion());
            put(body, "postcode", customPlaceUpdateRequest.getPostcode());
            put(body, "country", customPlaceUpdateRequest.getCountry());
            put(body, "website", customPlaceUpdateRequest.getWebsite());
            put(body, "tel", customPlaceUpdateRequest.getTel());
            put(body, "email", customPlaceUpdateRequest.getEmail());
            put(body, "tags", customPlaceUpdateRequest.getTags());
            put(body, "meta", customPlaceUpdateRequest.getMeta());
        } else if (request instanceof Models.CustomPlaceHideRequest hideRequest) {
            body.put("hidden", hideRequest.isHidden());
        } else if (request instanceof Models.CustomPlacesImportRequest importRequest) {
            put(body, "mode", importRequest.getMode());
            put(body, "groupId", importRequest.getGroupId());
            if (importRequest.getRows() != null) {
                body.put("rows", importRequest.getRows().stream().map(Mappers::importRowBody).toList());
            }
            if (importRequest.getPlaces() != null) {
                body.put("places", importRequest.getPlaces().stream().map(Mappers::importRowBody).toList());
            }
        } else if (request instanceof Models.OverrideRequest overrideRequest) {
            put(body, "groupId", overrideRequest.getGroupId());
            put(body, "hide", overrideRequest.getHide());
            put(body, "name", overrideRequest.getName());
            put(body, "latitude", overrideRequest.getLatitude());
            put(body, "longitude", overrideRequest.getLongitude());
            put(body, "address", overrideRequest.getAddress());
            put(body, "locality", overrideRequest.getLocality());
            put(body, "region", overrideRequest.getRegion());
            put(body, "postcode", overrideRequest.getPostcode());
            put(body, "country", overrideRequest.getCountry());
            put(body, "website", overrideRequest.getWebsite());
            put(body, "tel", overrideRequest.getTel());
            put(body, "email", overrideRequest.getEmail());
            put(body, "tags", overrideRequest.getTags());
            put(body, "meta", overrideRequest.getMeta());
        } else {
            throw new IllegalArgumentException("Unsupported request body: " + request.getClass().getName());
        }
        return body;
    }

    static Map<String, String> toQuery(Object request) {
        Map<String, String> query = new LinkedHashMap<>();
        if (request == null) {
            return query;
        }
        if (request instanceof Models.ListGroupsRequest listGroupsRequest) {
            putQuery(query, "limit", listGroupsRequest.getLimit());
        } else if (request instanceof Models.ListCustomPlacesRequest listCustomPlacesRequest) {
            putQuery(query, "groupId", listCustomPlacesRequest.getGroupId());
            putQuery(query, "limit", listCustomPlacesRequest.getLimit());
            putQuery(query, "cursor", listCustomPlacesRequest.getCursor());
            if (listCustomPlacesRequest.getIncludeHidden() != null) {
                query.put("includeHidden", listCustomPlacesRequest.getIncludeHidden() ? "1" : "0");
            }
        } else if (request instanceof Models.ExportCustomPlacesRequest exportCustomPlacesRequest) {
            putQuery(query, "groupId", exportCustomPlacesRequest.getGroupId());
            putQuery(query, "limit", exportCustomPlacesRequest.getLimit());
            putQuery(query, "cursor", exportCustomPlacesRequest.getCursor());
            if (exportCustomPlacesRequest.getIncludeHidden() != null) {
                query.put("includeHidden", exportCustomPlacesRequest.getIncludeHidden() ? "1" : "0");
            }
        } else if (request instanceof Models.NearestUsCitiesRequest nearestUsCitiesRequest) {
            putQuery(query, "lat", nearestUsCitiesRequest.getLat());
            putQuery(query, "lon", nearestUsCitiesRequest.getLon());
            putQuery(query, "limit", nearestUsCitiesRequest.getLimit());
        } else if (request instanceof Models.SearchUsCitiesRequest searchUsCitiesRequest) {
            putQuery(query, "q", searchUsCitiesRequest.getQ());
            putQuery(query, "state", searchUsCitiesRequest.getState());
            putQuery(query, "limit", searchUsCitiesRequest.getLimit());
        } else if (request instanceof Models.SearchCategoriesRequest searchCategoriesRequest) {
            putQuery(query, "q", searchCategoriesRequest.getQ());
            putQuery(query, "level", searchCategoriesRequest.getLevel());
            putQuery(query, "parentId", searchCategoriesRequest.getParentId());
            putQuery(query, "limit", searchCategoriesRequest.getLimit());
        } else if (request instanceof Models.CategoryChildrenRequest categoryChildrenRequest) {
            putQuery(query, "limit", categoryChildrenRequest.getLimit());
        } else if (request instanceof Models.SearchPlacesRequest searchPlacesRequest) {
            putQuery(query, "q", searchPlacesRequest.getQ());
            putQuery(query, "lat", searchPlacesRequest.getLat());
            putQuery(query, "lon", searchPlacesRequest.getLon());
            putQuery(query, "radiusMeters", searchPlacesRequest.getRadiusMeters());
            putQuery(query, "limit", searchPlacesRequest.getLimit());
            putQuery(query, "excludeCategoryMatch", searchPlacesRequest.getExcludeCategoryMatch());
            putQuery(query, "forceTypeahead", searchPlacesRequest.getForceTypeahead());
            putQuery(query, "customOnly", searchPlacesRequest.getCustomOnly());
            putQuery(query, "onlyCustom", searchPlacesRequest.getOnlyCustom());
            putQuery(query, "isAddress", searchPlacesRequest.getIsAddress());
            putQuery(query, "groupId", searchPlacesRequest.getGroupId());
        } else {
            throw new IllegalArgumentException("Unsupported query request: " + request.getClass().getName());
        }
        return query;
    }

    private static Models.PlanCatalogEntry planCatalogEntry(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.PlanCatalogEntry(
                string(map, "code"),
                string(map, "label"),
                nullableInteger(map.get("monthlyRequests")),
                nullableInteger(map.get("maxTenants")),
                nullableInteger(map.get("maxApps")),
                nullableInteger(map.get("maxApiKeys")),
                bool(map, "overageAllowed"),
                integer(map, "overageBlockRequests"),
                integer(map, "overageBlockPriceCents"),
                nullableInteger(map.get("maxUsageMultiplier")),
                bool(map, "hardCapByDefault"));
    }

    private static Models.Group group(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.Group(string(map, "group_id"), string(map, "tenant_id"), string(map, "name"), dateTime(map, "created_at"));
    }

    private static Models.Place place(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.Place(
                string(map, "fsq_place_id"),
                string(map, "place_source"),
                string(map, "name"),
                number(map, "latitude"),
                number(map, "longitude"),
                nullableString(map.get("address")),
                nullableString(map.get("locality")),
                nullableString(map.get("region")),
                nullableString(map.get("postcode")),
                nullableString(map.get("country")),
                nullableString(map.get("website")),
                nullableString(map.get("tel")),
                nullableString(map.get("email")),
                nullableString(map.get("formatted_address")),
                nullableString(map.get("geocode_provider")),
                nullableDouble(map.get("geocode_confidence")),
                nullableDateTime(map.get("created_at")),
                nullableDateTime(map.get("updated_at")));
    }

    private static Models.CustomPlace customPlace(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CustomPlace(
                string(map, "custom_place_id"),
                string(map, "tenant_id"),
                nullableString(map.get("app_id")),
                nullableString(map.get("group_id")),
                nullableString(map.get("owner_user_id")),
                string(map, "source"),
                nullableString(map.get("fsq_place_id")),
                string(map, "name"),
                number(map, "latitude"),
                number(map, "longitude"),
                nullableString(map.get("address")),
                nullableString(map.get("locality")),
                nullableString(map.get("region")),
                nullableString(map.get("postcode")),
                nullableString(map.get("country")),
                nullableString(map.get("website")),
                nullableString(map.get("tel")),
                nullableString(map.get("email")),
                map.get("tags"),
                map.get("meta"),
                dateTime(map, "created_at"),
                dateTime(map, "updated_at"),
                nullableDouble(map.get("dist_m")));
    }

    private static Models.ImportedCustomPlace importedCustomPlace(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.ImportedCustomPlace(
                string(map, "custom_place_id"),
                string(map, "tenant_id"),
                nullableString(map.get("app_id")),
                nullableString(map.get("group_id")),
                nullableString(map.get("owner_user_id")),
                string(map, "source"),
                nullableString(map.get("fsq_place_id")),
                string(map, "name"),
                number(map, "latitude"),
                number(map, "longitude"),
                nullableString(map.get("address")),
                nullableString(map.get("locality")),
                nullableString(map.get("region")),
                nullableString(map.get("postcode")),
                nullableString(map.get("country")),
                nullableString(map.get("website")),
                nullableString(map.get("tel")),
                nullableString(map.get("email")),
                map.get("tags"),
                map.get("meta"),
                dateTime(map, "created_at"),
                dateTime(map, "updated_at"),
                nullableDouble(map.get("dist_m")),
                nullableString(map.get("_import_action")));
    }

    private static Models.SearchResolved searchResolved(Object value) {
        if (value == null) {
            return null;
        }
        Map<String, Object> map = asMap(value);
        return new Models.SearchResolved(
                string(map, "mode"),
                nullableString(map.get("reason")),
                nullableString(map.get("prefix")),
                nullableString(map.get("categoryName")),
                nullableString(map.get("groupId")),
                nullableBoolean(map.get("customOnly")),
                nullableString(map.get("localityText")),
                nullableString(map.get("addressQuery")),
                nullableString(map.get("addressPlaceId")),
                nullableString(map.get("formattedAddress")),
                nullableString(map.get("geocodeProvider")),
                nullableBoolean(map.get("geocodeCacheHit")),
                nullableDouble(map.get("addressRadiusMeters")),
                nullableInteger(map.get("addressCandidateCount")),
                nullableInteger(map.get("addressFilteredCount")),
                searchResolvedCenter(map.get("center")));
    }

    private static Models.SearchResolvedCenter searchResolvedCenter(Object value) {
        if (value == null) {
            return null;
        }
        Map<String, Object> map = asMap(value);
        return new Models.SearchResolvedCenter(number(map, "lat"), number(map, "lon"), string(map, "source"), string(map, "kind"), string(map, "label"));
    }

    private static Models.SearchRow searchRow(Object value) {
        Map<String, Object> map = asMap(value);
        String source = string(map, "_source");
        if ("global".equals(source)) {
            return new Models.SearchRowGlobal(string(map, "fsq_place_id"), string(map, "name"), number(map, "latitude"),
                    number(map, "longitude"), number(map, "dist_m"), nullableString(map.get("category_name")), source);
        }
        return new Models.SearchRowCustom(customPlace(value), source);
    }

    private static Models.UsCity usCity(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.UsCity(integer(map, "id"), string(map, "city"), string(map, "state_id"), string(map, "state_name"),
                string(map, "zipcode"), number(map, "lat"), number(map, "lon"), nullableDouble(map.get("dist_m")));
    }

    private static Models.CategoryLookupRow category(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CategoryLookupRow(string(map, "category_id"), nullableString(map.get("category_name")),
                nullableString(map.get("category_label")), nullableInteger(map.get("category_level")),
                list(map, "hierarchy").stream().map(Mappers::hierarchyNode).toList());
    }

    private static Models.CategoryHierarchyNode hierarchyNode(Object value) {
        Map<String, Object> map = asMap(value);
        return new Models.CategoryHierarchyNode(integer(map, "level"), nullableString(map.get("category_id")), nullableString(map.get("category_name")));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value) {
        return (Map<String, Object>) value;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> list(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return List.of();
        }
        return (List<Object>) value;
    }

    private static String string(Map<String, Object> map, String key) {
        return String.valueOf(map.get(key));
    }

    private static String nullableString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private static boolean bool(Map<String, Object> map, String key) {
        return Boolean.TRUE.equals(map.get(key));
    }

    private static Boolean nullableBoolean(Object value) {
        return value == null ? null : Boolean.valueOf(String.valueOf(value));
    }

    private static int integer(Map<String, Object> map, String key) {
        return ((Number) map.get(key)).intValue();
    }

    private static Integer nullableInteger(Object value) {
        return value == null ? null : ((Number) value).intValue();
    }

    private static double number(Map<String, Object> map, String key) {
        return ((Number) map.get(key)).doubleValue();
    }

    private static Double nullableDouble(Object value) {
        return value == null ? null : ((Number) value).doubleValue();
    }

    private static OffsetDateTime dateTime(Map<String, Object> map, String key) {
        return OffsetDateTime.parse(String.valueOf(map.get(key)));
    }

    private static OffsetDateTime nullableDateTime(Object value) {
        return value == null ? null : OffsetDateTime.parse(String.valueOf(value));
    }

    private static void put(Map<String, Object> body, String key, Object value) {
        if (value != null) {
            body.put(key, value);
        }
    }

    private static Map<String, Object> importRowBody(Models.CustomPlaceImportRow row) {
        Map<String, Object> body = new LinkedHashMap<>();
        put(body, "customPlaceId", row.getCustomPlaceId());
        put(body, "custom_place_id", row.getCustomPlaceIdSnakeCase());
        put(body, "groupId", row.getGroupId());
        put(body, "group_id", row.getGroupIdSnakeCase());
        put(body, "source", row.getSource());
        put(body, "ownerUserId", row.getOwnerUserId());
        put(body, "owner_user_id", row.getOwnerUserIdSnakeCase());
        put(body, "fsqPlaceId", row.getFsqPlaceId());
        put(body, "fsq_place_id", row.getFsqPlaceIdSnakeCase());
        body.put("name", row.getName());
        body.put("latitude", row.getLatitude());
        body.put("longitude", row.getLongitude());
        put(body, "address", row.getAddress());
        put(body, "locality", row.getLocality());
        put(body, "region", row.getRegion());
        put(body, "postcode", row.getPostcode());
        put(body, "country", row.getCountry());
        put(body, "website", row.getWebsite());
        put(body, "tel", row.getTel());
        put(body, "email", row.getEmail());
        put(body, "tags", row.getTags());
        put(body, "meta", row.getMeta());
        return body;
    }

    private static void putQuery(Map<String, String> query, String key, Object value) {
        if (value != null) {
            query.put(key, String.valueOf(value));
        }
    }
}
