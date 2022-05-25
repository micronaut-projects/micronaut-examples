package example.offers;

import example.api.v1.Offer;
import example.api.v1.Pet;
import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class OffersControllerTest extends BaseRedisOfferTest{

    static Stream<Arguments> invalidArgumentsProvider() {
        return Stream.of(
                arguments(null, null, null, null),
                arguments("Dino", null, null, null),
                arguments("Dino", BigDecimal.valueOf(10.0), null, null),
                arguments("Dino", BigDecimal.valueOf(10.0), Duration.of(10, ChronoUnit.SECONDS), null),
                arguments("Dino", BigDecimal.valueOf(10.400), Duration.of(10, ChronoUnit.SECONDS), "desc"),
                arguments("Dino", BigDecimal.valueOf(10.0), null, "desc")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidArgumentsProvider")
    void saveInvalidArgumentsFails(String slug, BigDecimal price, Duration duration, String description) {
        assertThrows(ConstraintViolationException.class,
                () -> offersClient.save(slug, price, duration, description));
    }

    @Test
    void offerForNonExistentPetFails() {
        Optional<Offer> maybeOffer = Mono.from(offersClient.save(
                "not there",
                BigDecimal.valueOf(10.0),
                Duration.of(10, ChronoUnit.SECONDS),
                "description"
        )).log().blockOptional();

        assertThat(maybeOffer).isNotPresent();
    }

    @Test
    void saveOfferForExistentPet() {
        Pet pet = new Pet("Fred", "Harry",  "photo-1457914109735-ce8aba3b7a79.jpeg");
        petClientFallback.addPet(pet);

        Offer offer = Mono.from(
                offersClient.save(pet.getSlug(), BigDecimal.valueOf(10.0), Duration.of(10, ChronoUnit.SECONDS), "Friendly Dog")
        ).log().block();

        assertThat(offer).isNotNull();
        Pet offered = offer.getPet();
        assertThat(offered).isNotNull();
        assertThat(offered.getName()).isEqualTo("Harry");
        assertThat(offered.getVendor()).isEqualTo("Fred");
        assertThat(offer.getPrice()).isEqualTo(BigDecimal.valueOf(10.0));
        assertThat(offer.getDescription()).isEqualTo("Friendly Dog");
    }

    @Test
    void receiveRandomGetsOffer() {
        Offer offer = streamingHttpClient.jsonStream(HttpRequest.GET("/v1/offers"), Offer.class)
                .blockFirst();

        assertThat(offer).isNotNull();
        Pet offered = offer.getPet();
        assertThat(offered).isNotNull();
        assertThat(offered.getName()).isEqualTo("Harry");
        assertThat(offered.getVendor()).isEqualTo("Fred");
        assertThat(offer.getPrice()).isEqualTo(BigDecimal.valueOf(10.0));
        assertThat(offer.getDescription()).isEqualTo("Friendly Dog");
    }
}
