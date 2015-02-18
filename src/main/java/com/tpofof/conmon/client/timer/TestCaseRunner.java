package com.tpofof.conmon.client.timer;

import java.util.Collections;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.Device;
import com.pofof.conmon.model.DeviceConfiguration;
import com.pofof.conmon.model.TestCase;
import com.pofof.conmon.model.TimerResult;
import com.tpofof.conmon.client.config.DeviceManager;

public class TestCaseRunner implements Job {
	
	private GetAssetTimer getTimer = new GetAssetTimer();
	private HeadAssetTimer headTimer = new HeadAssetTimer();

	private Device getDevice() {
		Device d = DeviceManager.getDevice(false);
		if (d == null || !d.isRegistered()) {
			d = DeviceManager.getDevice(true);
		}
		return d;
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Device d = getDevice();
		if (d != null && d.isRegistered()) {
			DeviceConfiguration deviceConfig = DeviceManager.getCurrentConfig(true);
			// TODO: update interval http://quartz-scheduler.org/documentation/quartz-2.x/cookbook/UpdateTrigger
			List<TestCase> testCases = DeviceManager.getDeviceTestCases(true);
			if (deviceConfig != null &&  testCases != null) {
				for (TestCase tc : testCases) {
					if (deviceConfig.getTrialsCount() > 0) {
						TimerResult finalResult = runTrials(tc, getTimer, deviceConfig.getTrialsCount());
						if (!finalResult.isOutage()) {
							TimerResult pingResults = runTrials(tc, headTimer, deviceConfig.getTrialsCount());
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
		}
	}
	
	private TimerResult runTrials(TestCase tc, AssetTimer<?> timer, int trialsCount) {
		List<Long> durations = Lists.newArrayList();
		TimerResult finalResult = new TimerResult().setTestCaseId(tc.get_id());
		for (int i=0;i<trialsCount;i++) {
			TimerResult tempResult = timer.time(tc);
			// TODO: check that result is valid ensuring that all data was downloaded
			if (!tempResult.isOutage()) {				
				durations.add(tempResult.getDuration());
				finalResult.setStartTime(tempResult.getStartTime());
			} else {
				finalResult.setOutage(true);
				break;
			}
		}
		finalResult.setDuration((long)calcAvg(durations));
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
