package micronaut.neo4j.example.controller.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {

    private Integer id;
    private String name;
    private int year;

}
