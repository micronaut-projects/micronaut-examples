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
package example.storefront;

import example.api.v1.Pet;
import example.api.v1.PetType;
import example.api.v1.Vendor;
import example.storefront.client.v1.PetClient;
import example.storefront.client.v1.VendorClient;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class Application {
    private static Logger log = LoggerFactory.getLogger(Application.class);

    final PetClient petClient;
    final VendorClient vendorClient;

    public Application(PetClient petClient, VendorClient vendorClient) {
        this.petClient = petClient;
        this.vendorClient = vendorClient;
    }

    @EventListener
    void onStartup(ServerStartupEvent event) {

        // I can't seem to get the translated from Rxjava2 to Project Reactor
        // I am hung up with incompatable types at the end. The Groovy Rxjava2 version (see below) did a cast
        // that I don't understand working
        String[] names = {"Fred", "Arthur", "Joe"};
        List<Flux<Vendor>> saves = new ArrayList<>();
        for (String name: names) {
            saves.add(
                Flux.from(vendorClient.save(name)).flatMap(vendor -> {
                    List<Publisher<Pet>> operations = new ArrayList<>(); // List<Single<Pet>> operations
                    String vendorName = vendor.getName();
                    if ("Fred".equals(vendorName)) {
                        operations.add(petClient.save(new Pet(vendorName, "Harry", "photo-1457914109735-ce8aba3b7a79.jpeg").type(PetType.DOG)));
                        operations.add(petClient.save(new Pet(vendorName, "Ron", "photo-1442605527737-ed62b867591f.jpeg").type(PetType.DOG)));
                        operations.add(petClient.save(new Pet(vendorName, "Malfoy", "photo-1489911646836-b08d6ca60ffe.jpeg").type(PetType.CAT)));
                    } else if ("Arthur".equals(vendorName)) {
                        operations.add(petClient.save(new Pet(vendorName, "Hermione", "photo-1446231855385-1d4b0f025248.jpeg").type(PetType.DOG)));
                        operations.add(petClient.save(new Pet(vendorName, "Crabbe", "photo-1512616643169-0520ad604fc2.jpeg").type(PetType.CAT)));
                        operations.add(petClient.save(new Pet(vendorName, "Goyle", "photo-1505481354248-2ba5d3b9338e.jpeg").type(PetType.CAT)));
                    }
                    return null; // Single.merge(operations); //???
                }) // as Flowable<Vendor>)
            );
        }
        Flux.merge(saves).subscribe(
                result -> {},
                error -> log.error("An error occurred saving vendor data: {}}", error.getMessage(), error)
        );


//        def names = ["Fred", "Arthur", "Joe"]
//        List<Flowable<Vendor>> saves = []
//        for (name in names) {
//            saves.add(vendorClient.save(name).toFlowable().flatMap({ Vendor vendor ->
//                    List<Single<Pet>> operations = []
//                String vendorName = vendor.name
//                if (vendorName == 'Fred') {
//                    operations.add(petClient.save(new Pet(vendorName, "Harry", "photo-1457914109735-ce8aba3b7a79.jpeg").type(PetType.DOG)))
//                    operations.add(petClient.save(new Pet(vendorName, "Ron", "photo-1442605527737-ed62b867591f.jpeg").type(PetType.DOG)))
//                    operations.add(petClient.save(new Pet(vendorName, "Malfoy", "photo-1489911646836-b08d6ca60ffe.jpeg").type(PetType.CAT)))
//                } else if (vendorName == 'Arthur') {
//                    operations.add(petClient.save(new Pet(vendorName, "Hermione", "photo-1446231855385-1d4b0f025248.jpeg").type(PetType.DOG)))
//                    operations.add(petClient.save(new Pet(vendorName, "Crabbe", "photo-1512616643169-0520ad604fc2.jpeg").type(PetType.CAT)))
//                    operations.add(petClient.save(new Pet(vendorName, "Goyle", "photo-1505481354248-2ba5d3b9338e.jpeg").type(PetType.CAT)))
//                }
//                return Single.merge(operations)
//            }) as Flowable<Vendor>)
//        }
//        Flowable.merge(saves).subscribe({}, { Throwable e ->
//                log.error("An error occurred saving vendor data: ${e.message}", e)
//        })
    }

    static void main(String... args) {
        Micronaut.run(Application.class, args);
    }
}
