package co.charbox.rwsp.timer;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;

import co.charbox.domain.model.TestCase;
import co.charbox.domain.model.TimerResult;
import co.charbox.rwsp.HttpClientProvider;

public abstract class AssetTimer<HttpMethodT extends HttpMethodBase> {

	protected abstract HttpMethodT getHttpMethod(String assetLocation);
	
	public TimerResult time(TestCase testCase) {
		if (testCase == null) {
			throw new IllegalArgumentException("Test case cannot be null");
		}
		HttpMethodT httpMethod = getHttpMethod(testCase.getUri());
		httpMethod.addRequestHeader("Cache-Control", "no-cache");
		TimerResult res = new TimerResult()
			.setTestCaseId(testCase.get_id())
			.setStartTime(System.currentTimeMillis());
		try {
			HttpClientProvider.get().executeMethod(httpMethod);
			long endTime = System.currentTimeMillis();
			res.setDuration(endTime - res.getStartTime());
			res.setSize(httpMethod.getResponseContentLength());
			httpMethod.releaseConnection();
			return res;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		res.setOutage(true);
		return res;
	}
}
