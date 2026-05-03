package com.montjoy.places;

import java.util.List;
import java.util.Map;

public final class IntegrationSample {
    private IntegrationSample() {
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("MONTJOY_PLACES_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Set MONTJOY_PLACES_API_KEY before running the sample.");
        }

        MontjoyPlaces client = new MontjoyPlaces(apiKey);
        long suffix = System.currentTimeMillis();
        String groupName = "sdk-java-" + suffix;

        String groupId = null;
        String customPlaceId = null;

        try {
            Models.BillingPlansResponse plans = client.listBillingPlans();
            System.out.println("billing plans=" + plans.plans().stream().map(Models.PlanCatalogEntry::code).toList());

            Models.GroupSingleResponse createdGroup = client.createGroup(new Models.GroupCreateRequest(groupName));
            groupId = createdGroup.row().groupId();
            System.out.println("created group=" + createdGroup.row());

            Models.CustomPlaceCreateRequest createPlace = new Models.CustomPlaceCreateRequest(
                    "SDK Java Test Place " + suffix,
                    42.3601,
                    -71.0589)
                    .groupId(groupId)
                    .address("1 Beacon St")
                    .locality("Boston")
                    .region("MA")
                    .postcode("02108")
                    .country("US")
                    .website("https://example.com/java")
                    .tags(List.of("sdk", "java"))
                    .meta(Map.of("source", "integration-sample"));

            Models.CustomPlaceSingleResponse createdPlace = client.createCustomPlace(createPlace);
            customPlaceId = createdPlace.row().customPlaceId();
            System.out.println("created custom place=" + createdPlace.row());

            Models.CustomPlaceSingleResponse fetchedPlace = client.getCustomPlace(customPlaceId);
            System.out.println("fetched custom place=" + fetchedPlace.row());

            Models.CustomPlaceUpdateRequest updatePlace = new Models.CustomPlaceUpdateRequest()
                    .name("SDK Java Updated Place " + suffix)
                    .website("https://example.com/java-updated")
                    .meta(Map.of("source", "integration-sample", "updated", true));

            Models.CustomPlaceSingleResponse updatedPlace = client.updateCustomPlace(customPlaceId, updatePlace);
            System.out.println("updated custom place=" + updatedPlace.row());

            Models.CustomPlaceSingleResponse hiddenPlace = client.hideCustomPlace(customPlaceId, new Models.CustomPlaceHideRequest(true));
            System.out.println("hidden custom place=" + hiddenPlace.row());

            Models.CustomPlaceSingleResponse unhiddenPlace = client.hideCustomPlace(customPlaceId, new Models.CustomPlaceHideRequest(false));
            System.out.println("unhidden custom place=" + unhiddenPlace.row());

            Models.ListCustomPlacesRequest listRequest = new Models.ListCustomPlacesRequest()
                    .groupId(groupId)
                    .limit(10)
                    .includeHidden(true);

            Models.CustomPlacesListResponse listedPlaces = client.listCustomPlaces(listRequest);
            System.out.println("group custom places=" + listedPlaces.rows().stream().map(Models.CustomPlace::name).toList());

            Models.CustomPlacesExportResponse exportedPlaces = client.exportCustomPlaces(
                    new Models.ExportCustomPlacesRequest()
                            .groupId(groupId)
                            .limit(10)
                            .includeHidden(true)
            );
            System.out.println("exported custom places=" + exportedPlaces.count());

            Models.CustomPlacesImportResponse importedPlaces = client.importCustomPlaces(
                    new Models.CustomPlacesImportRequest()
                            .mode("upsert")
                            .rows(exportedPlaces.rows().stream()
                                    .map(row -> new Models.CustomPlaceImportRow(row.name(), row.latitude(), row.longitude())
                                            .customPlaceIdSnakeCase(row.customPlaceId())
                                            .groupIdSnakeCase(row.groupId())
                                            .fsqPlaceIdSnakeCase(row.fsqPlaceId())
                                            .ownerUserIdSnakeCase(row.ownerUserId())
                                            .source(row.source())
                                            .address(row.address())
                                            .locality(row.locality())
                                            .region(row.region())
                                            .postcode(row.postcode())
                                            .country(row.country())
                                            .website(row.website())
                                            .tel(row.tel())
                                            .email(row.email())
                                            .tags(row.tags())
                                            .meta(row.meta()))
                                    .toList())
            );
            System.out.println("imported custom places=" + importedPlaces.imported());

            Models.SearchResponse search = client.searchPlaces(new Models.SearchPlacesRequest("coffee near Boston MA").limit(3));
            String firstPlaceId = search.rows().stream()
                    .filter(Models.SearchRowGlobal.class::isInstance)
                    .map(Models.SearchRowGlobal.class::cast)
                    .map(Models.SearchRowGlobal::fsqPlaceId)
                    .findFirst()
                    .orElse(null);

            if (firstPlaceId != null) {
                Models.PlaceSingleResponse place = client.getPlace(firstPlaceId);
                System.out.println("direct place lookup=" + place.row());
            }
        } finally {
            if (customPlaceId != null) {
                try {
                    Models.DeleteResponse deletedPlace = client.deleteCustomPlace(customPlaceId);
                    System.out.println("deleted custom place=" + deletedPlace);
                } catch (Exception exception) {
                    System.out.println("cleanup failed for custom place=" + exception.getMessage());
                }
            }

            if (groupId != null) {
                try {
                    Models.GroupDeleteResponse deletedGroup = client.deleteGroup(groupId);
                    System.out.println("deleted group=" + deletedGroup);
                } catch (Exception exception) {
                    System.out.println("cleanup failed for group=" + exception.getMessage());
                }
            }
        }
    }
}
