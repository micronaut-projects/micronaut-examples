package example.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.OffsetStrategy;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultMockableListener implements MockableListener {
    public final static Logger logger = LoggerFactory.getLogger(DefaultExampleListener.class);

    @Inject
    ExampleService exampleService;


    @KafkaListener(offsetReset = OffsetReset.EARLIEST,  offsetStrategy = OffsetStrategy.DISABLED)
    //  Not topic here allows mocked bean dependency injection
    //@Topic("foo.bar")
    public void handleEvent(String event) {
        logger.info("Handling event: {}", event);
        exampleService.doEvent(event);
    }
}
