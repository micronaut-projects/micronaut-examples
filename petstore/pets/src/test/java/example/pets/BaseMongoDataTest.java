package example.pets;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.Map;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseMongoDataTest implements TestPropertyProvider {

    static MongoDBContainer mongoDBContainer;

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    PetControllerTestClient petClient;

    @Inject
    @Client("/")
    HttpClient httpClient;

    void startMongoDb() {
        if (mongoDBContainer == null) {
            mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.6"))
                    .withExposedPorts(27017);
        }
        if (!mongoDBContainer.isRunning()) {
            mongoDBContainer.start();
        }
    }

    String getMongoDbUri() {
        if (mongoDBContainer == null || !mongoDBContainer.isRunning()) {
            startMongoDb();
        }
        return mongoDBContainer.getReplicaSetUrl();
    }

    @Override
    @NotNull
    public Map<String, String> getProperties() {
        return Map.of(
                "mongodb.uri", getMongoDbUri(),
                "consul.client.enabled", "false"
        );
    }

    @AfterEach
    public void cleanup() throws IOException, InterruptedException {
        mongoDBContainer.execInContainer("mongosh", "--eval", "db.dropDatabase()");
    }

    @AfterAll
    public static void stop() {
        mongoDBContainer.close();
    }
}