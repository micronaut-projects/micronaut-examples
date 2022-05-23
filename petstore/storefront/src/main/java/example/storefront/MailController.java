package example.storefront;

import example.api.v1.HealthStatus;
import example.storefront.client.v1.MailClient;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Controller("/mail")
class MailController {

    private final EmailService emailService;

    private final MailClient mailClient;

    public MailController(EmailService emailService, MailClient mailClient) {
        this.emailService = emailService;
        this.mailClient = mailClient;
    }

    @Get("/health")
    Publisher<HealthStatus> health() {
        return Mono.from(mailClient.health()).onErrorReturn(new HealthStatus("DOWN"));
    }

    @Post(uri = "/send", consumes = MediaType.APPLICATION_JSON)
    HttpResponse<?> send(@Body("slug") String slug, @Body("email") String email) {
        emailService.send(slug, email);
        return HttpResponse.ok();
    }
}
