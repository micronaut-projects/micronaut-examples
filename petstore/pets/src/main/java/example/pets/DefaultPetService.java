package example.pets;

import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Singleton
class DefaultPetService implements PetService {

    private final PetRepository petRepository;

    public DefaultPetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Publisher<PetEntity> list() {
        return petRepository.findAll();
    }

    @Override
    public Publisher<PetEntity> save(PetEntity pet) {
        String slug = FriendlyUrl.sanitizeWithDashes(pet.getName());
        pet.slug(slug);
        return Flux.from(findBySlug(slug)).switchIfEmpty(petRepository.save(pet));
    }

    @Override
    public Publisher<PetEntity> random() {
        return petRepository.random();
    }

    @Override
    public Publisher<PetEntity> findByVendor(String name) {
        return petRepository.findByVendor(name);
    }

    @Override
    public Publisher<PetEntity> findBySlug(String slug) {
        return petRepository.findBySlug(slug);
    }
}
