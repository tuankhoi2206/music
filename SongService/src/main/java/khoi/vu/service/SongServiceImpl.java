package khoi.vu.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import khoi.vu.model.Song;

@Service
public class SongServiceImpl implements SongService {

	private static final Logger logger = LoggerFactory
			.getLogger(SongServiceImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	public Song findById(String id) {

		return mongoTemplate.findById(id, Song.class);
	}

	public boolean saveSong(Song song) {
		song.setSongId(UUID.randomUUID().toString());
		mongoTemplate.save(song);
		return true;
	}

	public boolean updateSong(Song song) {
		mongoTemplate.insert(song);
		return true;
	}

	public boolean deleteSongById(String id) {
		logger.info("receive deleteSongById " + id);
		Song song = new Song();
		song.setSongId(id);
		mongoTemplate.remove(song);
		return true;
	}

	public List<Song> findAllSongs() {
		List<Song> lstSong = mongoTemplate.findAll(Song.class);
		return lstSong != null ? lstSong : Collections.<Song> emptyList();
	}

}
