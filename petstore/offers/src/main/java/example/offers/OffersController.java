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
import io.micronaut.context.annotation.Value;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author graemerocher
 * @since 1.0
 */
@Controller("/${offers.api.version}/offers")
@Validated
public class OffersController implements OffersOperations {

    private final OffersRepository offersRepository;
    private final Duration offerDelay;


    public OffersController(OffersRepository offersRepository, @Value("${offers.delay:3s}") Duration offerDelay) {
        this.offersRepository = offersRepository;
        this.offerDelay = offerDelay;
    }

    /**
     * A non-blocking infinite JSON stream of offers that change every 10 seconds
     * @return A {@link Publisher} stream of JSON objects
     */
    @Get(uri = "/", produces = MediaType.APPLICATION_JSON_STREAM)
    public Publisher<Offer> current() {
        return Flux.from(offersRepository.random())
                .repeat(100)
                .delayElements(offerDelay);
    }

    /**
     * A non-blocking infinite JSON stream of offers that change every 10 seconds
     * @return A {@link Flux} stream of JSON objects
     */
    @Get(uri = "/all")
    public Publisher<Offer> all() {
        return offersRepository.all();
    }

    /**
     * Consumes JSON and saves a new offer
     *
     * @param slug Pet's slug
     * @param price The price of the offer
     * @param duration The duration of the offer
     * @param description The description of the offer
     * @return The offer or 404 if no pet exists for the offer
     */
    @Post("/")
    @Override
    @SingleResult
    public Publisher<Offer> save(String slug, BigDecimal price, Duration duration, String description) {
        return offersRepository.save(slug, price, duration, description);
    }
}
