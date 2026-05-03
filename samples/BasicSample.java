package com.montjoy.places;

public final class BasicSample {
    private BasicSample() {
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("MONTJOY_PLACES_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Set MONTJOY_PLACES_API_KEY before running the sample.");
        }

        MontjoyPlaces client = new MontjoyPlaces(apiKey);

        Models.BillingPlansResponse plans = client.listBillingPlans();
        System.out.println("billing plans=" + plans.plans().stream().map(Models.PlanCatalogEntry::code).toList());

        Models.WhoAmIResponse whoAmI = client.whoAmI();
        System.out.println("whoami tenant=" + whoAmI.tenantId() + " key=" + whoAmI.keyName() + " readOnly=" + whoAmI.readOnly());

        Models.GroupsListResponse groups = client.listGroups(new Models.ListGroupsRequest().limit(5));
        System.out.println("groups=" + groups.rows().stream().map(Models.Group::name).toList());

        Models.SearchResponse search = client.searchPlaces(new Models.SearchPlacesRequest("coffee near Boston MA").limit(3));
        System.out.println("search count=" + search.count());
        System.out.println(search.rows());

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
    }
}
