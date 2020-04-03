package example

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer

class HelloControllerTest: StringSpec() {

    val embeddedServer = autoClose(
            ApplicationContext.run(EmbeddedServer::class.java)
    )

    init {
        "test greeting service" {
            val helloClient =
                    embeddedServer.applicationContext.getBean(HelloClient::class.java)
                    helloClient.hello("John").blockingGet() shouldBe "Hello John"
        }
    }
}
