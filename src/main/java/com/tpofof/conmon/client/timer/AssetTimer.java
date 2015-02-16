package com.tpofof.conmon.client.timer;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.pofof.conmon.model.TestCase;

public class AssetTimer {

	private final HttpClient client = new HttpClient();
	
	public TimerResult time(TestCase testCase) {
		if (testCase == null) {
			throw new IllegalArgumentException("Test case cannot be null");
		}
		GetMethod gm = new GetMethod(testCase.getUri());
		TimerResult res = new TimerResult();
		res.setTestCase(testCase);
		try {
			res.setStartTime(System.currentTimeMillis());
			client.executeMethod(gm);
			long endTime = System.currentTimeMillis();
			res.setDuration(endTime - res.getStartTime());
			res.setSize(gm.getResponseContentLength());
			gm.releaseConnection();
			return res;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
