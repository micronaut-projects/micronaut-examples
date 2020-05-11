package micronaut.neo4j.example.service;

import micronaut.neo4j.example.controller.dto.AlbumDTO;
import micronaut.neo4j.example.controller.dto.MusicianDTO;
import micronaut.neo4j.example.domain.Album;
import micronaut.neo4j.example.domain.Musician;
import micronaut.neo4j.example.repository.MusicianRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class MusicianService {

    private MusicianRepository musicianRepository;

    public MusicianService(MusicianRepository musicianRepository) {
        this.musicianRepository = musicianRepository;
    }

    public MusicianDTO findMusicianById(@NotNull Integer id) {
        Musician musician = musicianRepository.findById(id);

        MusicianDTO result = new MusicianDTO();
        result.setId(musician.getId());
        result.setBorn(musician.getBorn());
        result.setName(musician.getName());

        Set<AlbumDTO> albums = new HashSet<>();
        for(Album a : musician.getAlbums()) {
            AlbumDTO aDTO = new AlbumDTO();
            aDTO.setId(a.getId());
            aDTO.setName(a.getName());
            aDTO.setYear(a.getYear());
            albums.add(aDTO);
        }
        result.setAlbums(albums);

        return result;
    }

    public void createMusician(MusicianDTO musicianDTO) {
        Musician musician = new Musician();
        musician.setId(musicianDTO.getId());
        musician.setName(musicianDTO.getName());
        musician.setBorn(musicianDTO.getBorn());
        Set<Album> albums = musicianDTO.getAlbums()
                .stream()
                .map(a -> new Album(a.getId(), a.getName(), a.getYear()))
                .collect(Collectors.toSet());
        musician.setAlbums(albums);

        musicianRepository.save(musician);
    }

}
