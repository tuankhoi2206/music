package khoi.vu.jms;

import java.util.HashMap;
import java.util.UUID;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import khoi.vu.model.Song;
import khoi.vu.service.SongServiceImpl;

@Service
public class JmsRequestor {

	private static final Logger logger = LoggerFactory.getLogger(SongServiceImpl.class);

	@Autowired
	@Qualifier("defaultDestination")
	private ActiveMQQueue queue;

	private final JmsTemplate jmsTemplate;

	@Autowired
	public JmsRequestor(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	private class ProducerConsumer implements SessionCallback<ObjectMessage> {

		private static final int TIMEOUT = 20000;
		private static final int TIME_EXPIRATION = 5000;

		private final DestinationResolver destinationResolver;

		private Song song;

		private String operation;

		public ProducerConsumer(Song msg, String operation, DestinationResolver destinationResolver,
				ActiveMQQueue queue) {
			this.song = msg;
			this.destinationResolver = destinationResolver;
			this.operation = operation;
		}

		public ObjectMessage doInJms(final Session session) throws JMSException {

			MessageConsumer consumer = null;
			MessageProducer producer = null;
			try {

				String correlationId = UUID.randomUUID().toString();

				final Destination replyQueue = destinationResolver.resolveDestinationName(session,
						queue.getQueueName() + ".response", false);

				HashMap<String, Object> packet = new HashMap<>();
				packet.put("operation", operation);
				packet.put("value", song);

				logger.info("JmsRequestor " + correlationId);

				ObjectMessage objMsg = session.createObjectMessage(packet);
				objMsg.setJMSCorrelationID(correlationId);
				objMsg.setJMSReplyTo(replyQueue);
				objMsg.setJMSExpiration(TIME_EXPIRATION);

				final Destination requestQueue = destinationResolver.resolveDestinationName(session,
						queue.getQueueName() + ".request", false);
				producer = session.createProducer(requestQueue);
				producer.send(requestQueue, objMsg);

				consumer = session.createConsumer(replyQueue, "JMSCorrelationID = '" + correlationId + "'");
				return (ObjectMessage) consumer.receive(TIMEOUT);

			} finally {
				JmsUtils.closeMessageConsumer(consumer);
				JmsUtils.closeMessageProducer(producer);
			}
		}

	}

	public ObjectMessage request(Song song, String operation) {
		return (ObjectMessage) jmsTemplate.execute(
				new JmsRequestor.ProducerConsumer(song, operation, jmsTemplate.getDestinationResolver(), this.queue),
				true);
	}

}