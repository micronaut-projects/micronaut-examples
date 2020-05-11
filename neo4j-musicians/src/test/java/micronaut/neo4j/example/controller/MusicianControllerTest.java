package micronaut.neo4j.example.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import micronaut.neo4j.example.controller.dto.AlbumDTO;
import micronaut.neo4j.example.controller.dto.MusicianDTO;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class MusicianControllerTest {

    @Inject
    @Client("/api/musician")
    private HttpClient client;

    @Test
    public void testCreateAndFindMusician() {
        Set<AlbumDTO> albums = new HashSet<>();
        AlbumDTO album = new AlbumDTO();
        album.setYear(1971);
        album.setName("Rory Gallagher");
        album.setId(1);
        albums.add(album);

        MusicianDTO musicianDTO = new MusicianDTO(1, "Rory Gallagher", 1948, albums);
        HttpRequest request = HttpRequest.POST("/", musicianDTO);

        assertEquals(HttpStatus.OK, client.toBlocking().exchange(request).getStatus());

        request = HttpRequest.GET("/1");
        MusicianDTO result = client.toBlocking().retrieve(request, MusicianDTO.class);

        assertEquals(1, result.getId());
        assertEquals("Rory Gallagher", result.getName());
        assertEquals(1948, result.getBorn());
        assertEquals(1, result.getAlbums().size());
    }

}
