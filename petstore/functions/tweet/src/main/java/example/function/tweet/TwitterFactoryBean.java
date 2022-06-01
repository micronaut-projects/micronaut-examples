/*
 * Copyright 2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.function.tweet;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.exceptions.ConfigurationException;
import jakarta.inject.Singleton;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;

/**
 * @author graemerocher
 * @since 1.0
 */
@Singleton
@Factory
public class TwitterFactoryBean {

    final TwitterConfiguration configuration;

    TwitterFactoryBean(TwitterConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean
    @Singleton
    Twitter twitter() {
        Configuration twitterConfig = configuration.builder.build();
        String oAuthAccessToken = twitterConfig.getOAuthAccessToken();
        if (oAuthAccessToken == null || oAuthAccessToken.isEmpty()) {
            throw new ConfigurationException("Missing Twitter OAuthAccessToken");
        }
        String oAuthAccessTokenSecret = twitterConfig.getOAuthAccessTokenSecret();
        if (oAuthAccessTokenSecret == null || oAuthAccessTokenSecret.isEmpty()) {
            throw new ConfigurationException("Missing Twitter OAuthAccessTokenSecret");
        }
        String oAuthConsumerKey = twitterConfig.getOAuthConsumerKey();
        if (oAuthConsumerKey == null || oAuthConsumerKey.isEmpty()) {
            throw new ConfigurationException("Missing Twitter OAuthConsumerKey");
        }
        String oAuthConsumerSecret = twitterConfig.getOAuthConsumerSecret();
        if (oAuthConsumerSecret == null || oAuthConsumerSecret.isEmpty()) {
            throw new ConfigurationException("Missing Twitter OAuthConsumerSecret");
        }
        TwitterFactory factory = new TwitterFactory(twitterConfig);
        return factory.getInstance();
    }
}
