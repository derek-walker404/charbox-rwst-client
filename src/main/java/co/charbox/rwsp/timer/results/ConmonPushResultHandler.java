package co.charbox.rwsp.timer.results;

import static co.charbox.rwsp.ApplicationSettings.CONMON_SERVER_URL_KEY;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.charbox.core.utils.Config;
import co.charbox.core.utils.JsonUtils;
import co.charbox.domain.model.TimerResult;
import co.charbox.rwsp.HttpClientProvider;

public class ConmonPushResultHandler extends TimerResultHandler {

	public static final Logger log = LoggerFactory.getLogger(ConmonPushResultHandler.class);	
	public static final String RESULTS_URL;
	
	static {
		Config config = Config.get();
		RESULTS_URL = config.getString(CONMON_SERVER_URL_KEY) + "/results";
	}
	
	public void handle(TimerResult result) {
		PostMethod pm = new PostMethod(RESULTS_URL);
		pm.addRequestHeader("Content-Type", "application/json");
		try {
			String jsonContent = JsonUtils.toJson(result);
			RequestEntity re = new StringRequestEntity(jsonContent, "application/json", "UTF-8");
			pm.setRequestEntity(re);
			int status = HttpClientProvider.get().executeMethod(pm);
			if (status != 200) {
				log.error("Failed to post timer result with status: " + status + "\t" + jsonContent);
			}
		} catch (Exception e) {
			log.error("Failed to post timer result with exception: " + e.getClass() + "\t" + e.getMessage());
		} finally {
			pm.releaseConnection();
		}
		// TODO: handle failed pushes
	}

}
