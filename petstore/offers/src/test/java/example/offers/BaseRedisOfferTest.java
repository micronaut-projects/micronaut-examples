package example.offers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.reactor.http.client.ReactorStreamingHttpClient;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;

import java.util.Map;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseRedisOfferTest implements TestPropertyProvider {

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    @Client("/${offers.api.version}/offers")
    TestOffersClient offersClient;

    @Inject
    @Client("/")
    ReactorStreamingHttpClient streamingHttpClient;

    @Inject
    OffersRepository offersRepository;

    @Inject
    TestPetClientFallback petClientFallback;

    static GenericContainer<?> redisContainer;

    void startRedis() {
        if (redisContainer == null) {
            redisContainer = new GenericContainer<>("redis:latest")
                    .withExposedPorts(6379);
        }
        if (!redisContainer.isRunning()) {
            redisContainer.start();
        }
    }

    String getRedisUri() {
        if (redisContainer == null || !redisContainer.isRunning()) {
            startRedis();
        }
        return String.format("redis://%s:%s", redisContainer.getHost(),  redisContainer.getFirstMappedPort());
    }

    @AfterAll
    static void stopRedis() {
        if (redisContainer.isRunning()) {
            redisContainer.close();
        }
    }

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return Map.of(
                "consul.client.enabled", "false"
                , "micronaut.http.client.read-timeout", "3m"
                , "offers.delay", "10ms"
                , "redis.uri", getRedisUri()
        );
    }
}
