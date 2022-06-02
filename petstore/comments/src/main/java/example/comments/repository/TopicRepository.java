package example.comments.repository;

import example.comments.domain.Topic;
import jakarta.inject.Singleton;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import javax.validation.constraints.NotBlank;

@Singleton
public class TopicRepository {

    private final SessionFactory sessionFactory;

    public TopicRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Topic findById(Long id) {
        return getSession().load(Topic.class, id);
    }

    /**
     * Finds a topic for the given title
     * @param title The title
     * @return The topic
     */
    // was findTopic()
    public Topic findByTitle(@NotBlank String title) {
        return getSession().load(Topic.class, title, 3);
    }

    /**
     * Finds a topic for the given title
     * @param title The title
     * @return The topic
     */
    public Topic saveTopic(@NotBlank String title) {
        Topic topic = new Topic().setTitle(title);
        saveOrUpdate(topic);
        return topic;
    }

    public void saveOrUpdate(Topic topic) {
        getSession().save(topic);
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }

}
