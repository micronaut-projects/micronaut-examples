package example

import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class HelloControllerTest {

    @Inject
    lateinit var helloClient: HelloClient

    @Test
    fun testGreetingService() {
        assertEquals(
                "Hello John",
                helloClient.hello("John").blockingGet()
        )
    }
}
