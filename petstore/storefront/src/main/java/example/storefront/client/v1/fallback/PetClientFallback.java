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
package example.storefront.client.v1.fallback;

import example.api.v1.Pet;
import example.api.v1.PetOperations;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Body;
import io.micronaut.retry.annotation.Fallback;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import java.util.Collections;


/**
 * @author graemerocher
 * @since 1.0
 */
@Fallback
public class PetClientFallback implements PetOperations<Pet> {
    @Override
    public Publisher<Pet> list() {
        return Flux.fromIterable(Collections.emptyList());
    }

    @Override
    public Publisher<Pet> findByVendor(String name) {
        return Flux.fromIterable(Collections.emptyList());
    }

    @Override
    @SingleResult
    public Publisher<Pet> random() {
        return Mono.empty();
    }

    @Override
    @SingleResult
    public Publisher<Pet> findBySlug(String slug) {
        return Mono.empty();
    }

    @Override
    @SingleResult
    public Publisher<Pet> save(@Valid @Body Pet pet) {
        return Mono.just(pet);
    }
}
