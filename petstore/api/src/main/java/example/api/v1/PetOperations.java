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
package example.api.v1;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.validation.Validated;
import org.reactivestreams.Publisher;

import javax.validation.Valid;

/**
 * @author graemerocher
 * @since 1.0
 */
@Validated
public interface PetOperations<T extends Pet> {

    @Get("/")
    Publisher<T> list();

    @Get("/random")
    @SingleResult
    Publisher<T> random();

    @Get("/vendor/{name}")
    Publisher<T> findByVendor(@PathVariable String name);

    @Get("/{slug}")
    @SingleResult
    Publisher<T> findBySlug(@PathVariable String slug);

    @Post("/")
    @Status(HttpStatus.CREATED)
    @SingleResult
    Publisher<T> save(@Valid @Body T pet);
}
