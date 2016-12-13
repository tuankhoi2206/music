package khoi.vu.repository;

import org.springframework.data.repository.CrudRepository;

import khoi.vu.model.Song;

public interface SongRepository extends CrudRepository<Song, String> {

}
