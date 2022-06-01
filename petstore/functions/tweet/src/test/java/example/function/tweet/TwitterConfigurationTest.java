package example.function.tweet;


import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import twitter4j.conf.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
@Property(name = "twitter.OAuth2AccessToken", value = "xxxxxx")
class TwitterConfigurationTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void twitterConfigManuallySupplied() {
        TwitterConfiguration config = applicationContext.getBean(TwitterConfiguration.class);
        Configuration configuration = config.builder.build();

        assertThat(configuration.getOAuth2AccessToken()).isEqualTo("xxxxxx");
    }
}
