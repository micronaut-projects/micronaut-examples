package example.comments.domain;

import io.micronaut.validation.Validated;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NodeEntity
@Validated
public class Topic extends Entity {

    @Id
    @NotBlank
    private String title;

    @Relationship(type = "comments")
    private List<Comment> comments = new ArrayList<>();

    public Topic() {}

    public Topic(String title, List<Comment> comments) {
        this.title = title;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public Topic setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic)) return false;

        Topic topic = (Topic) o;

        return Objects.equals(title, topic.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
