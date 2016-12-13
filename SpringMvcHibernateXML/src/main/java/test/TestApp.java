package test;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import khoi.vu.jms.JmsRequestor;
import khoi.vu.model.Operation;
import khoi.vu.model.Song;

public class TestApp {
	public static void main(String[] args) {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("servlet-context.xml",
				"spring-jms-context.xml");

		JmsRequestor requestor = (JmsRequestor) applicationContext.getBean("requestor");

		// SongJMS songJMS = new SongJMS(OPERATION.SEND_FINDBYID.toString(), new
		// Song("11"));
		// // requestor.request(new ProducerConsumer(songJMS));
		//
		// ObjectMessage obj = requestor.request(songJMS);
		// try {
		// SongJMS jms = (SongJMS) obj.getObject();
		// System.out.println(jms.getSongOperation() +
		// obj.getJMSCorrelationID());
		// } catch (JMSException e) {
		// e.printStackTrace();
		// }

	}
}
