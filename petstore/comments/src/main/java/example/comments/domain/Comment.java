package example.comments.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NodeEntity
public class Comment extends Entity implements example.api.v1.Comment {

    @NotBlank
    private String poster;

    @NotBlank
    private String content;

    @DateLong
    private final Date dateCreated;

    @JsonIgnore
    @Relationship(type = "replies")
    List<Comment> replies = new ArrayList<>();

    public Comment() {
        dateCreated = new Date();
    }

    @Override
    public String getPoster() {
        return poster;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    public Comment setPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public void addToReplies(Comment reply) {
        replies.add(reply);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;

        Comment comment = (Comment) o;

        return getId().equals(comment.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
