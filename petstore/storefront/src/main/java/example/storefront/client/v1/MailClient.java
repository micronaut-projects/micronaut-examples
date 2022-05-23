package example.storefront.client.v1;

import example.api.v1.Email;
import example.api.v1.HealthStatusOperation;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import javax.validation.Valid;

@Client(id = "mail")
public interface MailClient extends HealthStatusOperation {
    @Post("/v1/mail/send")
    HttpResponse<?> send(@Valid @Body Email email);
}
