package example.comments;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

@Factory
public class CommentsConfiguration {
    @Singleton
    public Configuration neo4jConfig() {
        return new Configuration.Builder().build();
    }

    @Singleton
    public SessionFactory sessionFactory(Configuration conf) {
        return new SessionFactory(conf, "example.comments.domain");
    }
}
