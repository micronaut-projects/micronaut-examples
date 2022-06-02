package example.comments.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

abstract class Entity {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public Entity setId(Long id) {
        this.id = id;
        return this;
    }
}
