package khoi.vu.service;

import java.util.List;

import khoi.vu.model.Song;

public interface SongService {

	Song findById(String id);

	boolean saveSong(Song Song);

	boolean updateSong(Song Song);

	boolean deleteSongById(String id);

	List<Song> findAllSongs();
}
