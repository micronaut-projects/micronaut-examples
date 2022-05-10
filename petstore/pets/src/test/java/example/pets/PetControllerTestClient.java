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
package example.pets;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author graemerocher
 * @since 1.0
 */
@Client("/${pets.api.version}/pets")
interface PetControllerTestClient /*extends PetOperations<PetEntity>*/ {
    @Get
    List<PetEntity> list();

    @Get("/{id}")
    Optional<PetEntity> find(@PathVariable String id);

    @Get("/random")
    Optional<PetEntity> random();

    @Post("/")
    HttpResponse<PetEntity> save(@Valid @Body PetEntity pet);

    @Get("/vendor/{name}")
    List<PetEntity> findByVendor(String name);

    @Get("/{slug}")
    Optional<PetEntity> findBySlug(String slug);
}
