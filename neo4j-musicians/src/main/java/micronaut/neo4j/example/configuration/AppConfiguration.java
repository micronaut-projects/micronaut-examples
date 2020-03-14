package micronaut.neo4j.example.configuration;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

@Factory
public class AppConfiguration {

    @Bean
    public Configuration neo4jConfig() {
        return new Configuration.Builder().build();
    }

    @Bean
    public SessionFactory sessionFactory(Configuration conf) {
        return new SessionFactory(conf, "micronaut.neo4j.example.domain");
    }

}
