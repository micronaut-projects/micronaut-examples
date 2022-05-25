package example.vendors;

import example.api.v1.Name;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@MicronautTest
@Property(name = "consul.client.enabled", value = "false")
public class VendorControllerTest {
    @Value("${vendors.api.version}")
    String apiVersion;

    @Inject
    @Client("/${vendors.api.version}/vendors")
    ReactorHttpClient client;

    @AfterEach
    void teardown() {
        // TODO is there a better way, like Spock's @Unroll for test methods
        client.toBlocking().exchange(HttpRequest.DELETE("/"));
    }

    @Test
    void nonExistentVendorReturns404() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(HttpRequest.GET("/99")));

        assertAll(
                () -> assertThat(exception.getResponse()).isNotNull(),
                () -> assertThat((CharSequence) exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND)
        );
    }

    @Test
    void listNoVendorsIsEmptyList() {
        HttpRequest<?> request = HttpRequest.GET("/list");
        List<Vendor> vendors = client.toBlocking().retrieve(request, Argument.listOf(Vendor.class));

        assertAll(
                () -> assertThat(vendors).isNotNull(),
                () -> assertThat(vendors).isEmpty()
        );
    }

    @Test
    void addVendorsAndListIsNotEmpty() {
        HttpRequest<?> request = HttpRequest.POST("/", Collections.singletonMap("name", "Pet Smart"));
        client.toBlocking().exchange(request);
        request = HttpRequest.POST("/", Collections.singletonMap("name", "Chewy"));
        client.toBlocking().exchange(request);
        request = HttpRequest.GET("/list");
        List<Vendor> vendors = client.toBlocking().retrieve(request, Argument.listOf(Vendor.class));

        assertAll(
                () -> assertThat(vendors).isNotNull(),
                () -> assertThat(vendors).isNotEmpty(),
                () -> assertThat(vendors).hasSize(2)
        );
    }

    @Test
    void testCrudOperations() {
        HttpRequest<?> request = HttpRequest.POST("/", Collections.singletonMap("name", "Pet Smart"));
        HttpResponse<Vendor> response = client.toBlocking().exchange(request, Vendor.class);

        // put new Vendor
        assertThat((CharSequence) response.getStatus()).isEqualTo(HttpStatus.CREATED);
        response.getBody().ifPresentOrElse(vendor -> {
            assertThat(vendor.getName()).isEqualTo("Pet Smart");
            assertThat(vendor.getId()).isGreaterThanOrEqualTo(1);
        }, () -> fail("Expected non-null vendor for 'Pet Smart'"));

        // lookup by id
        if (response.getBody().isPresent()) {
            Long id = response.getBody().get().getId();
            URI chewyUri = UriBuilder.of("/{id}")
                    .expand(Collections.singletonMap("id", String.valueOf(id)));
            Vendor vendor = client.toBlocking().retrieve(chewyUri.toString(), Vendor.class);
            assertThat(vendor.getName()).isEqualTo("Pet Smart");
        }

        // put another vendor and list
        request = HttpRequest.POST("/", Collections.singletonMap("name", "Chewy"));
        HttpResponse<Vendor> response2 = client.toBlocking().exchange(request, Vendor.class);
        assertThat((CharSequence) response.getStatus()).isEqualTo(HttpStatus.CREATED);
        request = HttpRequest.GET("/list");
        List<Vendor> vendors = client.toBlocking().retrieve(request, Argument.listOf(Vendor.class));
        assertThat(vendors).hasSize(2);

        // get vendor names
        request = HttpRequest.GET("/names");
        List<Name> vendorNames = client.toBlocking().retrieve(request, Argument.listOf(Name.class));
        assertThat(vendorNames).hasSize(2);
        List<String> names = vendorNames.stream().map(Name::getName).collect(Collectors.toList());
        assertThat(names).containsExactlyInAnyOrder("Chewy", "Pet Smart");
    }
}
