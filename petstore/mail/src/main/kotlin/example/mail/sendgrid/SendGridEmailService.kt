package example.mail.sendgrid

import com.sendgrid.*
import example.api.v1.Email
import example.mail.EmailService
import example.mail.MailController
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Singleton
import java.io.IOException

@Singleton
@Requires(property = "sendgrid.apiKey")
class SendGridEmailService : EmailService {

    @Value("\${sendgrid.apiKey}")
    internal var apiKey: String? = null

    @Value("\${sendgrid.fromEmail}")
    internal var fromEmail: String? = null

    protected fun contentOfEmail(email: Email): Content? {
        if (email.textBody != null) {
            return Content("text/plain", email.textBody)
        }
        return if (email.htmlBody != null) {
            Content("text/html", email.htmlBody)
        } else null
    }

    override fun send(email: Email) {

        val personalization = Personalization()
        personalization.subject = email.subject

        val to = com.sendgrid.Email(email.recipient)
        personalization.addTo(to)

        if (email.cc != null) {
            for (cc in email.cc) {
                val ccEmail = com.sendgrid.Email()
                ccEmail.email = cc
                personalization.addCc(ccEmail)
            }
        }

        if (email.bcc != null) {
            for (bcc in email.bcc) {
                val bccEmail = com.sendgrid.Email()
                bccEmail.email = bcc
                personalization.addBcc(bccEmail)
            }
        }

        val mail = Mail()
        val from = com.sendgrid.Email()
        from.email = fromEmail
        mail.setFrom(from)
        mail.addPersonalization(personalization)
        val content = contentOfEmail(email)
        mail.addContent(content!!)

        val sg = SendGrid(apiKey)
        val request = Request()
        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()
            val response = sg.api(request)
            log.info("Status Code: {}", response.statusCode.toString())
            log.info("Body: {}", response.body)
            for (key in response.headers.keys) {
                val value = response.headers[key]
                log.info("Response Header {} => {}", key, value)
            }
        } catch (ex: IOException) {
            log.error(ex.message)
        }

    }

    companion object {

        private val log = LoggerFactory.getLogger(MailController::class.java)
    }
}
