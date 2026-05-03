# MontjoyPlaces Java SDK

Official Java SDK for the Montjoy Places API.

- Homepage: https://montjoyplaces.com
- API Base URL: `https://api.montjoyplaces.com`
- Java Version: 17+
- License: MIT
- Support: paul@montjoyapp.com

## Installation

Use the following dependency coordinates when consuming the SDK from a Maven-compatible repository:

### Maven

```xml
<dependency>
  <groupId>com.montjoyplaces</groupId>
  <artifactId>MontjoyPlaces</artifactId>
  <version>1.0.3</version>
</dependency>
```

### Gradle

```gradle
implementation("com.montjoyplaces:MontjoyPlaces:1.0.3")
```

## Building From Source

Build the library:

```bash
mvn clean package
```

Install it into your local Maven repository:

```bash
mvn clean install
```

## Authentication

Create a client with your Montjoy Places API key:

```java
import com.montjoy.places.MontjoyPlaces;

MontjoyPlaces client = new MontjoyPlaces(System.getenv("MONTJOY_PLACES_API_KEY"));
```

The SDK sends the API key using the `X-API-Key` header.

## Quick Start

```java
import com.montjoy.places.Models;
import com.montjoy.places.MontjoyPlaces;

public class Example {
    public static void main(String[] args) {
        String apiKey = System.getenv("MONTJOY_PLACES_API_KEY");
        MontjoyPlaces client = new MontjoyPlaces(apiKey);

        Models.WhoAmIResponse whoAmI = client.whoAmI();
        System.out.println("tenant=" + whoAmI.tenantId() + " readOnly=" + whoAmI.readOnly());

        Models.SearchResponse search = client.searchPlaces(
                new Models.SearchPlacesRequest("coffee near Boston MA").limit(3)
        );

        System.out.println("results=" + search.count());
        System.out.println(search.rows());
    }
}
```

## Client Construction

The SDK supports three ways to create a client:

```java
import com.montjoy.places.MontjoyPlaces;

MontjoyPlaces defaultClient = new MontjoyPlaces(apiKey);
MontjoyPlaces customBaseUrlClient = new MontjoyPlaces(apiKey, "https://api.montjoyplaces.com");
```

You can also supply your own `HttpClient`:

```java
import com.montjoy.places.MontjoyPlaces;
import java.net.http.HttpClient;

HttpClient httpClient = HttpClient.newHttpClient();
MontjoyPlaces client = new MontjoyPlaces(apiKey, "https://api.montjoyplaces.com", httpClient);
```

This is useful if you want custom timeouts, proxies, connection pooling, or other transport-level configuration.

## Common Examples

### Check API Identity

```java
Models.WhoAmIResponse whoAmI = client.whoAmI();
System.out.println(whoAmI);
```

### List Groups

```java
Models.GroupsListResponse groups = client.listGroups(
        new Models.ListGroupsRequest().limit(10)
);
```

### Create a Group

```java
Models.GroupSingleResponse createdGroup = client.createGroup(
        new Models.GroupCreateRequest("Favorites")
);
```

### Search Places

```java
Models.SearchResponse search = client.searchPlaces(
        new Models.SearchPlacesRequest("pizza near New York NY")
                .limit(5)
                .radiusMeters(5000.0)
);
```

### Create a Custom Place

```java
import java.util.List;
import java.util.Map;

Models.CustomPlaceCreateRequest request = new Models.CustomPlaceCreateRequest(
        "My Custom Place",
        42.3601,
        -71.0589
)
        .groupId("group_123")
        .address("1 Beacon St")
        .locality("Boston")
        .region("MA")
        .postcode("02108")
        .country("US")
        .website("https://example.com")
        .tags(List.of("favorite", "team"))
        .meta(Map.of("source", "java-sdk"));

Models.CustomPlaceSingleResponse created = client.createCustomPlace(request);
```

### Export and Import Custom Places

```java
Models.CustomPlacesExportResponse exported = client.exportCustomPlaces(
        new Models.ExportCustomPlacesRequest()
                .groupId("group_123")
                .includeHidden(true)
);

Models.CustomPlacesImportResponse imported = client.importCustomPlaces(
        new Models.CustomPlacesImportRequest()
                .mode("upsert")
                .rows(exported.rows().stream()
                        .map(row -> new Models.CustomPlaceImportRow(row.name(), row.latitude(), row.longitude())
                                .customPlaceIdSnakeCase(row.customPlaceId())
                                .groupIdSnakeCase(row.groupId()))
                        .toList())
);
```

### Update a Custom Place

```java
Models.CustomPlaceUpdateRequest update = new Models.CustomPlaceUpdateRequest()
        .name("Updated Place Name")
        .website("https://example.com/updated");

Models.CustomPlaceSingleResponse updated = client.updateCustomPlace("custom_place_id", update);
```

### Hide or Unhide a Custom Place

```java
client.hideCustomPlace("custom_place_id", new Models.CustomPlaceHideRequest(true));
client.hideCustomPlace("custom_place_id", new Models.CustomPlaceHideRequest(false));
```

### Delete a Custom Place

```java
Models.DeleteResponse deleted = client.deleteCustomPlace("custom_place_id");
```

### Override a Place

```java
Models.OverrideRequest override = new Models.OverrideRequest()
        .groupId("group_123")
        .name("My Overridden Name")
        .hide(false);

Models.OverrideResponse response = client.overridePlace("fsq_place_id", override);
```

### Lookup US Cities

```java
Models.UsCityListResponse nearest = client.lookupNearestUsCities(
        new Models.NearestUsCitiesRequest(42.3601, -71.0589).limit(5)
);

Models.UsCitySearchResponse matches = client.searchUsCities(
        new Models.SearchUsCitiesRequest("Boston").state("MA").limit(5)
);

Models.UsZipLookupResponse zip = client.lookupUsZipcode("02108");
```

### Lookup Categories

```java
Models.CategorySearchResponse categories = client.searchCategories(
        new Models.SearchCategoriesRequest()
                .q("coffee")
                .limit(10)
);

Models.CategoryResponse category = client.getCategory("13032");

Models.CategoryChildrenResponse children = client.getCategoryChildren(
        "13032",
        new Models.CategoryChildrenRequest().limit(20)
);
```

## Supported Operations

The SDK currently includes methods for:

- API identity: `whoAmI`
- Groups: `listGroups`, `createGroup`, `updateGroup`, `deleteGroup`
- Custom places: `listCustomPlaces`, `exportCustomPlaces`, `importCustomPlaces`, `createCustomPlace`, `getCustomPlace`, `updateCustomPlace`, `deleteCustomPlace`, `hideCustomPlace`
- Place overrides: `overridePlace`
- US city lookup: `lookupNearestUsCities`, `searchUsCities`, `lookupUsZipcode`
- Category lookup: `searchCategories`, `getCategory`, `getCategoryChildren`
- Search: `searchPlaces`

## Error Handling

API errors are raised as `MontjoyPlacesException`.

```java
import com.montjoy.places.MontjoyPlacesException;

try {
    client.whoAmI();
} catch (MontjoyPlacesException exception) {
    System.out.println("status=" + exception.getStatusCode());
    System.out.println("message=" + exception.getMessage());
    System.out.println("body=" + exception.getResponseBody());
}
```

Transport-level failures such as networking issues are raised as `RuntimeException`.

## Data Model Style

The SDK uses:

- Java records for API responses
- Fluent request builders for optional request fields
- Standard Java `HttpClient` for transport

Examples:

- `new Models.SearchPlacesRequest("coffee").limit(5)`
- `new Models.CustomPlaceUpdateRequest().name("Updated")`

## Running the Samples

This repository includes two sample programs:

- [samples/BasicSample.java](/Users/pauldemers/Desktop/MontjoyPlaces-Java-SDK/samples/BasicSample.java)
- [samples/IntegrationSample.java](/Users/pauldemers/Desktop/MontjoyPlaces-Java-SDK/samples/IntegrationSample.java)

Set your API key first:

```bash
export MONTJOY_PLACES_API_KEY="your_api_key"
```

Then compile and run them using your preferred Java build workflow.

The integration sample exercises a larger CRUD flow:

- create a group
- create a custom place
- fetch and update it
- hide and unhide it
- list custom places
- export and import custom places
- clean up created records

## Project Structure

- [src/main/java/com/montjoy/places/MontjoyPlaces.java](/Users/pauldemers/Desktop/MontjoyPlaces-Java-SDK/src/main/java/com/montjoy/places/MontjoyPlaces.java): API client
- [src/main/java/com/montjoy/places/Models.java](/Users/pauldemers/Desktop/MontjoyPlaces-Java-SDK/src/main/java/com/montjoy/places/Models.java): request and response models
- [src/main/java/com/montjoy/places/MontjoyPlacesException.java](/Users/pauldemers/Desktop/MontjoyPlaces-Java-SDK/src/main/java/com/montjoy/places/MontjoyPlacesException.java): API exception type
- [samples/BasicSample.java](/Users/pauldemers/Desktop/MontjoyPlaces-Java-SDK/samples/BasicSample.java): basic usage example
- [samples/IntegrationSample.java](/Users/pauldemers/Desktop/MontjoyPlaces-Java-SDK/samples/IntegrationSample.java): end-to-end sample

## Development Notes

- The project is configured for Java 17 via `maven.compiler.release`.
- The package namespace is `com.montjoy.places`.
- The current version in `pom.xml` is `1.0.3`.

## License

MIT. See [LICENSE](/Users/pauldemers/Desktop/MontjoyPlaces-Java-SDK/LICENSE).
