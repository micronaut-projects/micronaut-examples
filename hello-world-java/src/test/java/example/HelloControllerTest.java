package example;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.inject.*;

@MicronautTest
public class HelloControllerTest {

	@Inject
	HelloClient helloClient;

    @Test
    public void testHello() throws Exception {
        assertEquals(helloClient.hello("Fred").blockingGet(), "Hello Fred!");
    }
}
