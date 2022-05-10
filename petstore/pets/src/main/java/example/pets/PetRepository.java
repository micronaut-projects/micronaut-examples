package example.pets;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.mongodb.annotation.MongoAggregateQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import org.reactivestreams.Publisher;

@MongoRepository
public interface PetRepository extends ReactiveStreamsCrudRepository<PetEntity, String> {

    @NonNull
    Publisher<PetEntity> findByVendor(@NonNull String name);

    @NonNull
    Publisher<PetEntity> findBySlug(@NonNull String slug);

    @MongoAggregateQuery("[{ $sample: { size: 1 } }]")
    Publisher<PetEntity> random();
}
