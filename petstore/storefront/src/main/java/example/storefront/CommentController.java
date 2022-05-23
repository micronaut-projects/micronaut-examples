package example.storefront;

import example.api.v1.HealthStatus;
import example.storefront.client.v1.Comment;
import example.storefront.client.v1.CommentClient;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author zacharyklein
 * @since 1.0
 */
@Controller("/comment")
public class CommentController {

    private final CommentClient commentClient;

    public CommentController(CommentClient commentClient) {
        this.commentClient = commentClient;
    }

    @Get("/health")
    public Publisher<HealthStatus> health() {

        return Mono.from(commentClient.health()).onErrorReturn(new HealthStatus("DOWN"));
    }

    @Get("/{topic}")
    public List<Comment> topics(String topic) {
        return commentClient.list(topic);
    }

    @Get("/{topic}/{id}")
    public Map<String, Object> thread(String topic, Long id) {
        return commentClient.expand(id);
    }

    @Post("/{topic}")
    public HttpStatus addTopic(String topic, @Body Comment comment) {
        return commentClient.add(topic, comment.getPoster(), comment.getContent());
    }

    @Post("/{topic}/{id}")
    public HttpStatus addReply(String topic, Long id, @Body Comment comment) {
        return commentClient.addReply(id, comment.getPoster(), comment.getContent());
    }
}
