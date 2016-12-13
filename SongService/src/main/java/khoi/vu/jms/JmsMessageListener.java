package khoi.vu.jms;

import java.io.Serializable;
import java.util.HashMap;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.JmsUtils;
import org.springframework.stereotype.Service;

import khoi.vu.model.OPERATION;
import khoi.vu.model.Song;
import khoi.vu.service.SongService;

@Service
public class JmsMessageListener implements
		SessionAwareMessageListener<ObjectMessage> {

	private static final Logger logger = LoggerFactory
			.getLogger(JmsMessageListener.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ActiveMQQueue queue;

	@Autowired
	private SongService songService;

	private static final int TIME_EXPIRATION = 5000;

	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(ObjectMessage message, Session session)
			throws JMSException {

		MessageProducer producer = null;

		try {

			Destination replyQueue = jmsTemplate
					.getDestinationResolver()
					.resolveDestinationName(
							session,
							queue.getQueueName().replace("request", "response"),
							false);

			HashMap<String, Object> packet = (HashMap<String, Object>) message
					.getObject();
			String operation = (String) packet.get("operation");
			Song song = (Song) packet.get("value");

			logger.info("JmsMessageListener receive : "
					+ message.getJMSCorrelationID() + " operation " + operation
					+ " value " + song != null ? " " : song.toString());

			ObjectMessage objMsg = null;
			if (OPERATION.DELETE.toString().equals(operation)) {
				objMsg = session.createObjectMessage(songService
						.deleteSongById(song.getSongId()));

			} else if (OPERATION.FIND.toString().equals(operation)) {
				objMsg = session.createObjectMessage(songService.findById(song
						.getSongId()));

			} else if (OPERATION.FIND_ALL.toString().equals(operation)) {
				objMsg = session.createObjectMessage((Serializable) songService
						.findAllSongs());

			} else if (OPERATION.SAVE.toString().equals(operation)) {
				objMsg = session
						.createObjectMessage(songService.saveSong(song));

			}

			objMsg.setJMSCorrelationID(message.getJMSCorrelationID());
			objMsg.setJMSExpiration(TIME_EXPIRATION);
			producer = session.createProducer(replyQueue);
			producer.send(replyQueue, objMsg);

			logger.info("JmsMessageListener send : "
					+ objMsg.getJMSCorrelationID() + " value "
					+ objMsg.getObject().toString());

		} finally {
			JmsUtils.closeMessageProducer(producer);
		}
	}
}