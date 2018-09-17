package example.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.OffsetStrategy;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Requires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Requires("")
public class DefaultExampleListener implements ExampleListener {
    public final static Logger logger = LoggerFactory.getLogger(DefaultExampleListener.class);

    @Inject
    ExampleService exampleService;


    @KafkaListener(offsetReset = OffsetReset.EARLIEST,  offsetStrategy = OffsetStrategy.DISABLED)
    @Topic("foo.bar")  //  Topic here disallows mocked dependency injection
    public void handleEvent(String event) {
        logger.info("Handling event: {}", event);
        exampleService.doEvent(event);
    }
}
