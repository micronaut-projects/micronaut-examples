package example.pets;

import org.reactivestreams.Publisher;

interface PetService {

    Publisher<PetEntity> list();

    Publisher<PetEntity> save(PetEntity pet);

    Publisher<PetEntity> random();

    Publisher<PetEntity> findByVendor(String name);

    Publisher<PetEntity> findBySlug(String slug);
}
