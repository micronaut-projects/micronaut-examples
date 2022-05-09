package example.mail;

import com.sendgrid.Request;
import com.sendgrid.Response;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static io.micronaut.http.HttpStatus.ACCEPTED;

@Requires(property = "spec.name", value = "MailControllerTest")
@Singleton
@Replaces(AsyncEmailSender.class)
@Named(EmailSenderReplacement.NAME)
class EmailSenderReplacement implements AsyncTransactionalEmailSender<Request, Response> {

    public static final String NAME = "sendgrid";

    final List<Email> emails = new ArrayList<>();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public Publisher<Response> sendAsync(@NonNull @Valid Email email,
                                         @NonNull Consumer<Request> emailRequest) throws EmailException {
        emails.add(email);
        Response response = new Response();
        response.setStatusCode(ACCEPTED.getCode());
        return Mono.just(response);
    }

    public List<Email> getEmails() {
        return emails;
    }
}