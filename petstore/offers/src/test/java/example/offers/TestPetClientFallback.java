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

import example.api.v1.Pet;
import example.offers.client.v1.PetClient;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.retry.annotation.Fallback;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author graemerocher
 * @since 1.0
 */
@Fallback
@Singleton
class TestPetClientFallback implements PetClient {

    private final Map<String, Pet> pets = new HashMap<>();

    void addPet(Pet pet) {
        pets.put(pet.getSlug(), pet);
    }

    @Override
    @SingleResult
    public Publisher<Pet> findBySlug(String slug) {
        Pet pet = pets.get(slug);
        return Mono.justOrEmpty(pet);
    }

    @Override
    public Publisher<Pet> list() {
        return Flux.fromIterable(Collections.emptyList());
    }
}
