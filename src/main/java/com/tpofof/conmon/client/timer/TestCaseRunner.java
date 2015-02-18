package com.tpofof.conmon.client.timer;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.client.ConmonConfig;

public class TestCaseRunner implements Job {
	
	private static final Logger log = Logger.getLogger("TestCaseRunner");
	
	private ConmonConfig config = ConmonConfig.getDefaultConfig();
	private GetAssetTimer getTimer = new GetAssetTimer();
	private HeadAssetTimer headTimer = new HeadAssetTimer();

	public void execute(JobExecutionContext context) throws JobExecutionException {
		for (TestCase tc : config.getTestCases()) {
			if (config.getTrialsCount() > 0) {
				TimerResult finalResult = runTrials(tc, getTimer);
				if (!finalResult.isOutage()) {
					TimerResult pingResults = runTrials(tc, headTimer);
					if (pingResults.isOutage()) {
						finalResult.setOutage(true);
					} else {
						finalResult.setPingDuration(pingResults.getDuration());
					}
				}
				handleTimerResult(finalResult);
			}
		}
	}
	
	private TimerResult runTrials(TestCase tc, AssetTimer timer) {
		List<Long> durations = Lists.newArrayList();
		TimerResult finalResult = new TimerResult().setTestCaseId(tc.get_id());
		for (int i=0;i<config.getTrialsCount();i++) {
			TimerResult tempResult = timer.time(tc);
			if (!tempResult.isOutage()) {				
				durations.add(tempResult.getDuration());
				finalResult.setStartTime(tempResult.getStartTime());
			} else {
				finalResult.setOutage(true);
				break;
			}
		}
		finalResult.setDuration((long)calcAvg(durations));
		handleTimerResult(finalResult);
		return finalResult;
	}
	
	private double calcAvg(List<Long> durations) {
		Collections.sort(durations);
		double a = 0;
		int end = durations.size();
		if (durations.size() > 2) {
			end = durations.size() - 1;
		}
		for (int i=0;i<end;i++) {
			a += durations.get(i);
		}
		return a / end;
	}
	
	public void handleTimerResult(TimerResult res) {
		System.out.println(res);
	}
}
