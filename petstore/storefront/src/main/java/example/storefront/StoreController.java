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
package example.storefront;

import example.api.v1.Offer;
import example.api.v1.Pet;
import example.api.v1.Vendor;
import example.storefront.client.v1.CommentClient;
import example.storefront.client.v1.PetClient;
import example.storefront.client.v1.TweetClient;
import example.storefront.client.v1.VendorClient;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.sse.Event;
import io.micronaut.reactor.http.client.ReactorStreamingHttpClient;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

/**
 * @author graemerocher
 * @since 1.0
*/
@Controller("/")
public class StoreController {

    private final ReactorStreamingHttpClient offersClient;
    private final VendorClient vendorClient;
    private final PetClient petClient;
    private final CommentClient commentClient;
    private final TweetClient tweetClient;

    public StoreController(
            @Client(id = "offers") ReactorStreamingHttpClient offersClient,
            VendorClient vendorClient,
            PetClient petClient,
            CommentClient commentClient,
            TweetClient tweetClient) {
        this.offersClient = offersClient;
        this.vendorClient = vendorClient;
        this.petClient = petClient;
        this.commentClient = commentClient;
        this.tweetClient = tweetClient;
    }

    @Produces(MediaType.TEXT_HTML)
    @Get(uri = "/")
    public HttpResponse<?> index() {
        return HttpResponse.redirect(URI.create("/index.html"));
    }

    @Get(uri = "/offers", produces = MediaType.TEXT_EVENT_STREAM)
    @SingleResult
    public Publisher<Event<Offer>> offers() {
        return Mono.from(offersClient.jsonStream(HttpRequest.GET("/v1/offers"), Offer.class)).map(Event::of);
    }

    @Get("/pets")
    public Publisher<Pet> pets() {
        // FIXME: not sure how to make this work. Flux.from(petClient.list()).onErrorReturn(p) takes a single Pet as arg
        //  we don't want to return a null or bogus Pet object.
        // old: Single<List<Pet>> petClient.list().onErrorReturnItem(Collections.emptyList())
//        return Flux.from(petClient.list()).onErrorReturn(Collections.emptyList());

        // maybe just leave it as is
        return petClient.list();
    }

    @Get("/pets/{slug}")
    @SingleResult
    public Publisher<Pet> showPet(@PathVariable("slug") String slug) {
        return petClient.findBySlug(slug);
    }

    @Get("/pets/random")
    @SingleResult
    public Publisher<Pet> randomPet() {
        return petClient.random();
    }


    @Get("/pets/vendor/{vendor}")
    public Publisher<Pet> petsForVendor(String vendor) {
        // FIXME: not sure how to make this work. Flux.from(petClient.findByVendor(vendor)).onErrorReturn() takes a single Pet as arg
        //  we don't want to return a null or bogus Pet object.
        // old: Single<List<Pet>> petClient.byVendor(vendor).onErrorReturnItem(Collections.emptyList());
//        return Flux.from(petClient.findByVendor(vendor)).onErrorReturn(Collections.emptyList());

        // maybe just leave it as is
        return petClient.findByVendor(vendor);
    }

    @Get("/vendors")
    public Publisher<Vendor> vendors() {
        // FIXME: not sure how to make this work. Flux.from(vendorClient.list()).onErrorReturn() takes a single Vendor as arg
        //  we don't want to return a null or bogus Vendor object.
// old: Single<List<Vendor>> vendorClient.list().onErrorReturnItem(Collections.emptyList());
//        return Flux.from(vendorClient.list()).onErrorReturn(Collections.emptyList());

        // maybe just leave it as is
        return vendorClient.list();
    }
}
