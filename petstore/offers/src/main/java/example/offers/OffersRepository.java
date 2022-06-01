/*
 * Copyright 2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.offers;

import example.api.v1.Offer;
import example.api.v1.Pet;
import example.offers.client.v1.PetClient;
import io.lettuce.core.KeyValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.core.convert.value.ConvertibleValues;
import io.micronaut.validation.Validated;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author graemerocher
 * @since 1.0
 */
@Singleton
@Validated
public class OffersRepository implements OffersOperations {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private final PetClient petClient;
    private final StatefulRedisConnection<String, String> redisConnection;

    public OffersRepository(PetClient petClient, StatefulRedisConnection<String, String> redisConnection) {
        this.petClient = petClient;
        this.redisConnection = redisConnection;
    }

    /**
     * @return Returns all current offers
     */
    public Publisher<Offer> all() {
        RedisReactiveCommands<String, String> commands = redisConnection.reactive();

        return commands.keys("*").flatMap(keyToOffer(commands));
    }
    /**
     * @return Obtain a random offer or return {@link Mono#empty()} if there is none
     */
    @SingleResult
    public Publisher<Offer> random() {
        RedisReactiveCommands<String, String> commands = redisConnection.reactive();
        return commands.randomkey().flatMap(keyToOffer(commands));
    }

    /**
     * Save an offer for the given pet, vendor etc.
     *
     * @param slug pet's slug
     * @param price The price
     * @param duration The duration of the offer
     * @param description The description of the offer
     * @return The offer if it was possible to save it as a {@link Mono} or a empty {@link Mono} if no pet exists to create the offer for
     */
    @Override
    @SingleResult
    public Publisher<Offer> save(
            String slug,
            BigDecimal price,
            Duration duration,
            String description) {

        Publisher<Pet> pet = petClient.findBySlug(slug);
        return Mono.from(pet).flatMap(petInstance -> {
            ZonedDateTime expiryDate = ZonedDateTime.now().plus(duration);
            Offer offer = new Offer(petInstance,description,price);
            Map<String, String> data = dataOf(price, description, offer.getCurrency());
            String key = petInstance.getSlug();
            RedisReactiveCommands<String, String> redisApi = redisConnection.reactive();
            return redisApi.hmset(key,data)
                    .flatMap(success-> redisApi.expireat(key, expiryDate.toEpochSecond()))
                    .map(ok -> offer);
        });
    }

    /**
     * Create initial offers for the application
     */
    public void createInitialOffers() {
        try {
            redisConnection.sync().flushall();
        } catch (Exception e) {
            LOG.error("Error flushing Redis data: {}", e.getMessage(), e);
        }

        LOG.info("Creating Initial Offers for Pets: {}",
                Flux.from(petClient.list()).collectList().block());

        saveAnOffer("harry", new BigDecimal("49.99"),
                Duration.of(2, ChronoUnit.HOURS), "Cute dog!");
        saveAnOffer("malfoy", new BigDecimal("29.99"),
                Duration.of(2, ChronoUnit.HOURS), "Special Cat! Offer ends soon!");
        saveAnOffer("goyle", new BigDecimal("39.99"),
                Duration.of(1, ChronoUnit.DAYS), "Carefree Cat! Low Maintenance! Looking for a Home!");
    }

    private void saveAnOffer(String slug, BigDecimal price, Duration duration, String description ) {
        Mono.from(petClient.findBySlug(slug))
                .doOnError(throwable -> LOG.error("No pet found: " + throwable.getMessage(), throwable))
                .onErrorStop()
                .subscribe(pet -> Mono.from(save(pet.getSlug(), price, duration,description))
                        .subscribe((offer) -> {}, throwable ->
                                LOG.error("Error occurred saving offer: {}", throwable.getMessage(), throwable)));
    }

    private Map<String, String> dataOf(BigDecimal price, String description, Currency currency) {
        return Map.of(
                "currency", currency.getCurrencyCode(),
                "price", price.toString(),
                "description", description
        );
    }

    private Function<String, Mono<? extends Offer>> keyToOffer(RedisReactiveCommands<String, String> commands) {
        return key -> {
            Flux<KeyValue<String, String>> values = commands.hmget(key, "price", "description");
            Map<String, String> map = new HashMap<>(3);
            return values.reduce(map, (all, keyValue) -> {
                all.put(keyValue.getKey(), keyValue.getValue());
                return all;
            }).map(ConvertibleValues::of).flatMap(entries -> {
                String description = entries.get("description", String.class)
                        .orElseThrow(() -> new IllegalStateException("No description"));
                BigDecimal price = entries.get("price", BigDecimal.class)
                        .orElseThrow(() -> new IllegalStateException("No price"));

                Publisher<Pet> petForSlug = petClient.findBySlug(key);
                return Mono.from(petForSlug).map(pet -> new Offer(pet, description, price));
            });
        };
    }
}
