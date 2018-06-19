package example.mail

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

import java.util.Collections
import org.junit.jupiter.api.Assertions

class MailControllerTest : Spek({

    describe("MailController") {
        val server = ApplicationContext.run(EmbeddedServer::class.java, Collections.singletonMap<String, Any>(
                "consul.client.enabled", false
        ))
        val client = server!!
                .applicationContext
                .createBean(HttpClient::class.java, server.url)

        it("should be able to accept send request") {
            val requestBody = "{\"recipient\": \"sergio.delamo@softamo.com\"}"
            val rsp = client!!.toBlocking().exchange<String, Any>(HttpRequest.POST("/v1/mail/send", requestBody))

            Assertions.assertEquals(rsp.status.code.toLong(), 200)
        }

        afterGroup {
            server.stop()
        }
    }

})