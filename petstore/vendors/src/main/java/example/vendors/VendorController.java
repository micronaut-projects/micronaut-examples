/*
 * Copyright 2022 original authors
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
package example.vendors;

import example.api.v1.Name;
import example.api.v1.Pet;
import example.api.v1.VendorOperations;
import example.vendors.client.v1.PetClient;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller("/${vendors.api.version}/vendors")
class VendorController implements VendorOperations<Vendor> {

    private final VendorRepository vendorRepository;
    private final PetClient petClient;

    @Value("${vendors.api.version}")
    String apiVersion;

    VendorController(VendorRepository vendorRepository, PetClient petClient) {
        this.vendorRepository = vendorRepository;
        this.petClient = petClient;
    }

    @Get("/{id}")
    @SingleResult
    Publisher<Vendor> show(Long id) {
        return vendorRepository.findById(id);
    }

    @Get("/list")
    public Publisher<Vendor> list() {

        // my feeble attempt to translate the old code (see original RxJava2 Groovy code below)
        Mono.fromCallable(() -> vendorRepository.findAll())
                .subscribeOn(Schedulers.boundedElastic())
                .flux()
                .flatMap(Flux::from)
                .flatMap(vendor ->
                        // <screaming face> this is wrong, I'm not sure how to translate from old code
                        Flux.from(petClient.findByVendor(vendor.getName()))
                                .map(pet -> {
                                    List<Pet> pets = vendor.getPets();
                                    pets.add(pet);
                                    return pets;
                                }).collectList());

        // TODO this needs to fetch the Pets via petClient.findByVendor(name) for each vendor
        //  see the old reactive code below
        return vendorRepository.findAll();


            // original Pet Store impl
        //        @Get('/')
        //        Single<List<Vendor>> list() {
        //            return Single.fromCallable({-> vendorService.list() })
        //                .subscribeOn(Schedulers.io())
        //                .toFlowable()
        //                .flatMap({ List<Vendor> list ->
        //                        Flowable.fromIterable(list)
        //                })
        //                .flatMap({ Vendor v -> petClient.byVendor(v.name).map({ List<Pet> pets ->
        //                    return v.pets(pets)
        //                }).toFlowable()
        //            })
        //            .toList()
        //        }
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Publisher<Vendor> save(@Body("name") @NotBlank String name) {
        return Flux.from(vendorRepository.findByName(name)).switchIfEmpty(vendorRepository.save(name));
    }

    @Get("/names")
    public Publisher<Name> names() {
        return vendorRepository.findAll().map(v -> new Name(v.getName()));
    }

    @Delete("/")
    public Publisher<Long> deleteAll() {
        return vendorRepository.deleteAll();
    }
}
