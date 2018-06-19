package example.mail.ses

import javax.inject.Inject
import javax.inject.Singleton

import java.io.IOException

import com.amazonaws.AmazonClientException
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import example.api.v1.Email
import example.mail.EmailService
import example.mail.MailController
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
@Requires(beans = arrayOf(AWSCredentialsProviderService::class))
class AWSSESMailService : EmailService {

    @Value("\${aws.ses.region}")
    internal var awsRegion: String? = null

    @Value("\${aws.sourceEmail}")
    internal var sourceEmail: String? = null

    @Inject
    internal var awsCredentialsProvider: AWSCredentialsProvider? = null

    private fun bodyOfEmail(email: Email): Body {
        if (email.htmlBody != null) {
            val htmlBody = Content().withData(email.htmlBody)
            return Body().withHtml(htmlBody)
        }
        if (email.textBody != null) {
            val textBody = Content().withData(email.textBody)
            return Body().withHtml(textBody)
        }
        return Body()
    }

    override fun send(email: Email) {

        if (awsCredentialsProvider == null) {
            log.warn("AWS Credentials provider not configured")
            return
        }

        var destination = Destination().withToAddresses(email.recipient)
        if (email.cc != null) {
            destination = destination.withCcAddresses(email.cc)
        }
        if (email.bcc != null) {
            destination = destination.withBccAddresses(email.bcc)
        }
        val subject = Content().withData(email.subject)
        val body = bodyOfEmail(email)
        val message = Message().withSubject(subject).withBody(body)

        var request = SendEmailRequest()
                .withSource(sourceEmail)
                .withDestination(destination)
                .withMessage(message)

        if (email.replyTo != null) {
            request = request.withReplyToAddresses()
        }

        try {
            log.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...")

            val client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(awsCredentialsProvider)
                    .withRegion(awsRegion)
                    .build()

            client.sendEmail(request)
            log.info("Email sent!")

        } catch (ex: Exception) {
            log.warn("The email was not sent.")
            log.warn("Error message: " + ex.message)
        }

    }

    companion object {
        private val log = LoggerFactory.getLogger(MailController::class.java)
    }
}

