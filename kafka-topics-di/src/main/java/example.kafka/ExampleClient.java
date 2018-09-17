package example.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

//  Were using this class to send messages via kafka
@KafkaClient
public interface ExampleClient {
    @Topic("foo.bar")
    void send(String event);
}
