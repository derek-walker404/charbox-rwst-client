package com.tpofof.conmon.client.timer;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;

import com.pofof.conmon.model.TestCase;
import com.pofof.conmon.model.TimerResult;
import com.tpofof.conmon.client.HttpClientProvider;

public abstract class AssetTimer<HttpMethodT extends HttpMethodBase> {

	protected abstract HttpMethodT getHttpMethod(String assetLocation);
	
	public TimerResult time(TestCase testCase) {
		if (testCase == null) {
			throw new IllegalArgumentException("Test case cannot be null");
		}
		HttpMethodT httpMethod = getHttpMethod(testCase.getUri());
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
