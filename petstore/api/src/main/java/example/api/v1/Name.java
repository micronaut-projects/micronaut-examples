package example.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class Name {
    @JsonValue
    private final String name;

    @JsonCreator
    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
