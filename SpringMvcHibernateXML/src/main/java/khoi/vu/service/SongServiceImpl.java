package khoi.vu.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import khoi.vu.jms.JmsRequestor;
import khoi.vu.model.Operation;
import khoi.vu.model.Song;

@Service
public class SongServiceImpl implements SongService {

	private static final Logger logger = LoggerFactory.getLogger(SongServiceImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired(required = true)
	private JmsRequestor jmsRequestor;

	@SuppressWarnings("unchecked")
	public Song findById(String id) {
		logger.info("findById()");
		try {
			ObjectMessage objectMessage = jmsRequestor.request(new Song(id), Operation.FIND.toString());
			if (objectMessage != null) {
				HashMap<String, Object> packet = null;
				packet = (HashMap<String, Object>) objectMessage.getObject();
				Song song = (Song) packet.get("value");
				logger.info(objectMessage.getJMSCorrelationID().toString() + " findById :" + song.toString());
				return song;
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return null;

	}

	public boolean saveSong(Song song) {
		logger.info("saveSong()");
		try {
			ObjectMessage objectMessage = jmsRequestor.request(song, Operation.SAVE.toString());

			if (objectMessage != null) {

				boolean isSecces = (boolean) objectMessage.getObject();
				logger.info(objectMessage.getJMSCorrelationID().toString() + " saveSong :" + song.toString());
				return isSecces;
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateSong(Song song) {
		mongoTemplate.insert(song);
		return true;
	}

	public boolean deleteSongById(final String id) {
		logger.info("deleteSongById()");
		try {
			ObjectMessage objectMessage = jmsRequestor.request(new Song(id), Operation.DELETE.toString());

			if (objectMessage != null) {

				boolean isSecces = (boolean) objectMessage.getObject();
				logger.info(objectMessage.getJMSCorrelationID().toString() + " deleteSongById : " + id);
				return isSecces;
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<Song> findAllSongs() {
		logger.info("findAllSongs()");
		List<Song> songs = null;
		try {
			ObjectMessage objectMessage = jmsRequestor.request(null, Operation.FIND_ALL.toString());
			if (objectMessage != null) {
				songs = (List<Song>) objectMessage.getObject();

				logger.info(objectMessage.getJMSCorrelationID().toString() + " findAllSongs " + songs.size());
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return songs != null ? songs : Collections.<Song>emptyList();
	}

}
