package khoi.vu.controller;

import java.io.IOException;
import java.util.List;

import org.atmosphere.cpr.AtmosphereResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import khoi.vu.atmosphere.AtmosphereUtils;
import khoi.vu.model.Song;
import khoi.vu.service.SongService;

@RestController
public class SongController {

	private static final Logger logger = LoggerFactory.getLogger(SongController.class);

	@Autowired
	private SongService songService;

	@RequestMapping(value = "/song/", method = RequestMethod.GET)
	public List<Song> getAllSong() {
		logger.info("getAllSong()");
		return songService.findAllSongs();
	}

	@RequestMapping(value = "/song/{id}", method = RequestMethod.GET)
	public Song getSong(@PathVariable("id") String songId) {
		logger.info("getSong()");
		return songService.findById(songId);
	}

	// @RequestMapping(value = "/song/", method = RequestMethod.POST)
	// public void createSong(final AtmosphereResource event, @RequestBody Song
	// song)
	// throws JsonGenerationException, JsonMappingException, IOException {
	//
	// logger.info("createSong()");
	// if (songService.saveSong(song)) {
	// AtmosphereUtils.post(event, "add Song: " + song.getSongName());
	// }
	// }
	//
	// @RequestMapping(value = "/song/", method = RequestMethod.PUT)
	// public void updateSong(final AtmosphereResource event, @RequestBody Song
	// song)
	// throws JsonGenerationException, JsonMappingException, IOException {
	// logger.info("updateSong()");
	//
	// if (songService.updateSong(song)) {
	// AtmosphereUtils.post(event, "update Song:" + song.getSongName());
	// }
	// }
	//
	// @RequestMapping(value = "/song/{id}", method = RequestMethod.DELETE)
	// public void deleteSong(final AtmosphereResource event,
	// @PathVariable("id") String songId)
	// throws JsonGenerationException, JsonMappingException, IOException {
	// logger.info("deleteSong()");
	//
	// if (songService.deleteSongById(songId)) {
	// AtmosphereUtils.post(event, "delete Song:" + songId);
	// }
	// }

}
