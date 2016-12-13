package khoi.vu.main;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestApp {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.err.println("SongService");
		new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/spring/appServlet/spring-jms-context.xml");
	}
}
