package micronaut.neo4j.example.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import micronaut.neo4j.example.controller.dto.MusicianDTO;
import micronaut.neo4j.example.service.MusicianService;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller("/api/musician")
public class MusicianController {

    private MusicianService musicianService;

    public MusicianController(MusicianService musicianService) {
        this.musicianService = musicianService;
    }

    @Get(uri = "/{musicianId}")
    public MusicianDTO getMusician(@NotNull Integer musicianId) {
        return musicianService.findMusicianById(musicianId);
    }

    @Post
    public void createMusician(MusicianDTO musicianDTO) {
        musicianService.createMusician(musicianDTO);
    }

}
