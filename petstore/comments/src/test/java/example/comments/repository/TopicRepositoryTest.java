package example.comments.repository;

import example.comments.domain.Comment;
import example.comments.domain.Topic;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
@Testcontainers
@Property(name = "consul.client.enabled", value = "false")
@Property(name = "neo4j.embedded.ephemeral", value = "true")
public class TopicRepositoryTest {

    @Container
    private static Neo4jContainer<?> neo4jContainer =
            new Neo4jContainer<>(DockerImageName.parse("neo4j:4.4")).withoutAuthentication();

    @Inject
    SessionFactory sessionFactory;

    @Inject
    TopicRepository topicRepository;
    @Inject
    CommentRepository commentRepository;

    @Inject
    TestCommentClient testCommentClient;

    @Test
    void testSomethingUsingBolt() {

        try (Driver driver = GraphDatabase.driver(neo4jContainer.getBoltUrl(), AuthTokens.none())) {
            Session session = driver.session();
            long one = session.run("RETURN 1", Collections.emptyMap()).next().get(0).asLong();
            assertThat(one).isEqualTo(1L);
        }
    }

    @Test
    void saveTopicCreatesATopic() {
        Topic foo = topicRepository.saveTopic("Foo");
        assertThat(foo).isNotNull();
        String title = foo.getTitle();
        Topic bar = topicRepository.findByTitle(title);
        assertThat(bar).isEqualTo(foo);
        assertThat(foo).isNotSameAs(bar);
    }

    @Test
    void saveBadTopicNameFails() {
        assertThrows(ValidationException.class, () -> topicRepository.saveTopic(""));
    }

    @Test
    void saveAndRetrieveTopicComments() {
        // A new topic with comments is saved
        Comment comment = commentRepository.saveComment(
                "Some Topic", "Fred", "Some content"
        );

        Topic bar = topicRepository.findByTitle("Some Topic");
        assertThat(bar).isNotNull();
        assertThat(bar.getComments()).contains(comment);
    }

    @Test
    void saveAndRetrieveComments() {

        // A new topic with comments is saved
//        topicRepository.saveTopic("Some Topic");
        Comment comment1 = commentRepository.saveComment(
                "Some Topic", "Fred", "Some content"
        );

        Topic bar = topicRepository.findByTitle("Some Topic");
        assertThat(bar).isNotNull();
        assertThat(bar.getComments()).contains(comment1);

        Comment reply1 = commentRepository.saveReply(comment1, "Barney", "I Agree!");
        Comment reply2 = commentRepository.saveReply(comment1, "Jeff", "Absolutely!");
        Comment reply3 = commentRepository.saveReply(reply1, "John", "Yup!");

        Comment comment2 = commentRepository.saveComment(
                "Some Topic", "Joe", "Some content"
        );
        Comment reply4 = commentRepository.saveReply(comment2, "Ed", "Superb!");

//        Collection<Comment> all = commentRepository.findAll();
        List<Comment> comments = commentRepository.findComments("Some Topic");
        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getDateCreated()).isNotNull();
        assertThat(comments.get(0).getPoster()).isEqualTo("Fred");

        // TODO commentRepository.findReplies() is not yet implemented
        Map<String, Object> replies = commentRepository.findReplies(comment1.getId());

        assertThat(replies.get("id")).isEqualTo(comment1.getId());
        assertThat((List<Map>)replies.get("replies")).hasSize(2);

        // from legacy Groovy version of test
//        replies.replies.first().replies.size() == 1
//        !replies.replies.last().replies
    }

    @Test
    void readTopicResponse() {
        List<Comment> comments = testCommentClient.list("Some Topic");
        assertThat(comments).isNotNull();
        assertThat(comments).hasSize(2);
        Comment first = comments.get(0);
        assertThat(first.getPoster()).isEqualTo("Fred");

        Map<String,Object> data = testCommentClient.expand(first.getId());
        assertThat(data.get("id")).isEqualTo(first.getId());
        assertThat((List<Map>)data.get("replies")).hasSize(2);

        // A new reply is added
        HttpStatus status = testCommentClient.addReply(first.getId(), "Edward", "More stuff");
        assertThat((CharSequence) status).isEqualTo(HttpStatus.CREATED);

        data = testCommentClient.expand(first.getId());
        assertThat((List<Map>)data.get("replies")).hasSize(3);

    }
}
