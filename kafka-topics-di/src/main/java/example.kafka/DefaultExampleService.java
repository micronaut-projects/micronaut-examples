package example.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class DefaultExampleService implements ExampleService {
    public final static Logger logger = LoggerFactory.getLogger(DefaultExampleService.class);

    @Override
    public void doEvent(String event) {
        logger.info("Doing event {}", event);
    }
}
