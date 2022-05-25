package example.offers;

import example.api.v1.Offer;
import example.api.v1.Pet;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OffersRepositoryTest extends BaseRedisOfferTest {

    @Test
    void offerForNonExistentPetFails() {
        Optional<Offer> maybeOffer = Mono.from(offersRepository.save(
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
                offersRepository.save(pet.getSlug(), BigDecimal.valueOf(10.0), Duration.of(10, ChronoUnit.SECONDS), "Friendly Dog")
        ).log().block();

        assertThat(offer).isNotNull();
        Pet offered = offer.getPet();
        assertThat(offered).isNotNull();
        assertThat(offered.getName()).isEqualTo("Harry");
        assertThat(offered.getVendor()).isEqualTo("Fred");
        assertThat(offer.getPrice()).isEqualTo(BigDecimal.valueOf(10.0));
        assertThat(offer.getDescription()).isEqualTo("Friendly Dog");

        offer = Mono.from(offersRepository.random()).block();

        assertThat(offer).isNotNull();
        offered = offer.getPet();
        assertThat(offered).isNotNull();
        assertThat(offered.getName()).isEqualTo("Harry");
        assertThat(offered.getVendor()).isEqualTo("Fred");
        assertThat(offer.getPrice()).isEqualTo(BigDecimal.valueOf(10.0));
        assertThat(offer.getDescription()).isEqualTo("Friendly Dog");
    }
}
