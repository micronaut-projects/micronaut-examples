package micronaut.neo4j.example.controller.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class MusicianDTO {

    private Integer id;
    private String name;
    private int born;

    private Set<AlbumDTO> albums = new HashSet<>();

}
