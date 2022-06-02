package example.comments.controller;

import example.api.v1.CommentOperations;
import example.comments.domain.Comment;
import example.comments.repository.CommentRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;

import java.util.List;
import java.util.Map;

@Controller("/${comments.api.version}/topics")
public class CommentController implements CommentOperations<Comment> {

    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> list(String topic) {
        return commentRepository.findComments(topic);
    }

    @Override
    public Map<String, Object> expand(Long id) {
        return commentRepository.findReplies(id);
    }

    @Override
    public HttpStatus add(String topic, String poster, String content) {
        Comment c = commentRepository.saveComment(
                topic, poster, content
        );
        return (c != null) ? HttpStatus.CREATED : HttpStatus.NOT_FOUND;
    }

    @Override
    public HttpStatus addReply(Long id, String poster, String content) {
        Comment c = commentRepository.saveReply(
                id, poster, content
        );
        return (c != null) ? HttpStatus.CREATED : HttpStatus.NOT_FOUND;
    }
}
