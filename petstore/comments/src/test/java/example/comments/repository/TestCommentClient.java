package example.comments.repository;

import example.api.v1.CommentOperations;
import example.comments.domain.Comment;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.NotBlank;

@Client("/${comments.api.version}/topics")
public interface TestCommentClient extends CommentOperations<Comment> {
    @Override
    HttpStatus addReply(@NotBlank Long id, @NotBlank String poster, @NotBlank String content);
}
