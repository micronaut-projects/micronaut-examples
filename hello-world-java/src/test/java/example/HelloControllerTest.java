package example;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloControllerTest {

    @Test
    public void testHello() throws Exception {
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class);

        HelloClient helloClient = server.getApplicationContext().getBean(HelloClient.class);

        assertEquals(helloClient.hello("Fred").blockingGet(), "Hello Fred!");
        server.stop();
    }

    @Test
    public void testHello2() throws Exception {
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class);

        HelloClient helloClient = server.getApplicationContext().getBean(HelloClient.class);

        assertEquals(helloClient.hello("Fred").blockingGet(), "Hello Fred!");
        server.stop();
    }

    @Test
    public void testHello3() throws Exception {
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class);

        HelloClient helloClient = server.getApplicationContext().getBean(HelloClient.class);

        assertEquals(helloClient.hello("Fred").blockingGet(), "Hello Fred!");
        server.stop();
    }
}
