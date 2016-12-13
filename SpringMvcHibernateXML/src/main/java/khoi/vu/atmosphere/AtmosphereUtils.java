package khoi.vu.atmosphere;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.Meteor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import khoi.vu.model.StatusMessage;

public final class AtmosphereUtils {

	public static final Logger LOG = LoggerFactory.getLogger(AtmosphereUtils.class);

	public static AtmosphereResource getAtmosphereResource(HttpServletRequest request) {
		return getMeteor(request).getAtmosphereResource();
	}

	public static Meteor getMeteor(HttpServletRequest request) {
		return Meteor.build(request);
	}

	/**
	 * note : suspend:
	 * 
	 * @param resource
	 */
	public static void suspend(final AtmosphereResource resource) {

		final CountDownLatch countDownLatch = new CountDownLatch(1);
		resource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
			@Override
			public void onSuspend(AtmosphereResourceEvent event) {
				countDownLatch.countDown();
				LOG.info("Suspending Client..." + resource.uuid());
				resource.removeEventListener(this);
			}

			@Override
			public void onDisconnect(AtmosphereResourceEvent event) {
				LOG.info("Disconnecting Client..." + resource.uuid());
				super.onDisconnect(event);
			}

			@Override
			public void onBroadcast(AtmosphereResourceEvent event) {
				LOG.info("Client is broadcasting..." + resource.uuid());
				super.onBroadcast(event);
			}

		});

		// Add a AtmosphereResource to the list of item to be notified when the
		// Broadcaster.broadcast is invoked.
		AtmosphereUtils.lookupBroadcaster().addAtmosphereResource(resource);

		if (AtmosphereResource.TRANSPORT.LONG_POLLING.equals(resource.transport())) {
			resource.resumeOnBroadcast(true).suspend(-1, false);
		} else {
			resource.suspend(-1);
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			LOG.error("Interrupted while trying to suspend resource {}", resource);
		}
	}

	public static Broadcaster lookupBroadcaster() {
		/*
		 * create channels (or Broadcasters) on the fly, using the
		 * BroadcasterFactory class.
		 */
		Broadcaster b = BroadcasterFactory.getDefault().get();
		// LOG.error("lookupBroadcaster {}",b./ );
		return b;
	}

	@Autowired
	// @Qualifier("jsonMessageConverter")
	private static ObjectMapper objectMapper;

	// example add song
	public static void post(final AtmosphereResource event, String songMsg)
			throws JsonGenerationException, JsonMappingException, IOException {

		LOG.info("Received message to broadcast: {}", songMsg);

		StatusMessage messages = new StatusMessage(songMsg);

		/*
		 * Khi nhận được một thông báo, nó sẽ được chuyến tiếp (broadcast) đến
		 * tất cả các nơi đã được đăng ký trong trình phát quảng bá
		 * (broadcaster).
		 */

		/*
		 * A Broadcaster implements the publish/subscribe paradigm. An
		 * application can subscribe to one or many Broadcasters to get notified
		 * about events.
		 */
		/*
		 * send notify
		 */
		event.getBroadcaster().broadcast(objectMapper.writeValueAsString(messages));
	}
}
