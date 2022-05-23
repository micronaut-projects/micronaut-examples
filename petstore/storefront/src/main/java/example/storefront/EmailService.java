package example.storefront;

import example.api.v1.Email;
import example.api.v1.Pet;
import example.storefront.client.v1.MailClient;
import example.storefront.client.v1.PetClient;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class EmailService {

    private final PetClient petClient;
    private final MailClient mailClient;

    EmailService(PetClient petClient, MailClient mailClient) {
        this.petClient = petClient;
        this.mailClient = mailClient;
    }

    void send(String slug, String email) {
        Mono.from(petClient.findBySlug(slug))
            .subscribe(
                pet -> sendEmail(email, pet),
                error -> {}
            );
    }

    void sendEmail(String email, Pet pet) {
        Email emailDTO = new Email()
            .recipient(email)
            .subject(String.format("Micronaut Pet Store - Re: %s from %s", pet.getName(), pet.getVendor()));
        mailClient.send(emailDTO);
    }
}
