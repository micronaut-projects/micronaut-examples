package example.mail;

import com.sendgrid.Request;
import com.sendgrid.Response;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.Email;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller("/${mail.api.version}/mail")
public class MailController {
    private static final Logger log = LoggerFactory.getLogger(MailController.class);

    private final AsyncEmailSender<Request, Response> emailSender;

    public MailController(AsyncEmailSender<Request, Response> emailSender) {
        this.emailSender = emailSender;
    }

    @Post("/send")
    @SingleResult
    public Publisher<HttpResponse<?>> send(@Body example.api.v1.Email email) {
        log.info(email.toString());

        // TODO make this better, and more complete
        Email.Builder builder = Email.builder()
                .to(email.getRecipient())
                .subject(email.getSubject())
                .body(email.getHtmlBody(), email.getTextBody());

        return Mono.from(emailSender.sendAsync(builder))
                .doOnNext(rsp -> {
                    log.info("response status {}\nresponse body {}\nresponse headers {}",
                            rsp.getStatusCode(), rsp.getBody(), rsp.getHeaders());
                }).map(rsp -> rsp.getStatusCode() >= 400 ?
                        HttpResponse.unprocessableEntity() :
                        HttpResponse.accepted());
    }
}