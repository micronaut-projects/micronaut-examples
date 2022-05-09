package example.mail;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.micronaut.email.BodyType.HTML;
import static io.micronaut.email.BodyType.TEXT;
import static io.micronaut.http.HttpStatus.ACCEPTED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "MailControllerTest")
@Property(name = "consul.client.enabled", value = "false")
@MicronautTest
public class MailControllerTest {

    @Inject
    @Client("/")
    private HttpClient httpClient;

    @Inject
    BeanContext beanContext;

    @Test
    void getMailSendEndpointSendsAnEmail() {

        String requestBody = "{\"cc\": [\"wetted@objectcomputing.com\"],\"recipient\": \"wetted@objectcomputing.com\", " +
                "\"subject\": \"Interested in Pet\", \"replyTo\": \"wetted@objectcomputing.com\", " +
                "\"htmlBody\": \"Body html\", \"textBody\": \"Body text\",\"bcc\": [\"wetted@objectcomputing.com\"]}";

        HttpResponse<?> response = httpClient.toBlocking().exchange(HttpRequest.POST("/v1/mail/send", requestBody));
        assertEquals(ACCEPTED, response.status());

        AsyncTransactionalEmailSender<?, ?> sender = beanContext.getBean(AsyncTransactionalEmailSender.class);
        assertTrue(sender instanceof EmailSenderReplacement);

        EmailSenderReplacement sendgridSender = (EmailSenderReplacement) sender;
        assertTrue(CollectionUtils.isNotEmpty(sendgridSender.emails));
        assertEquals(1, sendgridSender.emails.size());

        Email email = sendgridSender.emails.get(0);
        assertEquals(email.getFrom().getEmail(), "wetted@objectcomputing.com");
        assertNotNull(email.getTo());
        assertTrue(email.getTo().stream().findFirst().isPresent());
        assertEquals(email.getTo().stream().findFirst().get().getEmail(), "wetted@objectcomputing.com");
        assertEquals(email.getSubject(), "Interested in Pet");
        assertNotNull(email.getBody());
        assertTrue(email.getBody().get(HTML).isPresent());
        assertEquals(email.getBody().get(HTML).get(), "Body html");
        assertTrue(email.getBody().get(TEXT).isPresent());
        assertEquals(email.getBody().get(TEXT).get(), "Body text");
    }
}