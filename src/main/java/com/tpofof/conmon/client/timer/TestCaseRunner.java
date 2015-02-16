package com.tpofof.conmon.client.timer;

import java.util.Date;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.client.ConmonConfig;

public class TestCaseRunner implements Job {
	
	private static final Logger log = Logger.getLogger("TestCaseRunner");
	
	private ConmonConfig config = ConmonConfig.getDefaultConfig();
	private AssetTimer timer = new AssetTimer();

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("\n\n");
		log.info("Running Test Case Runner: " + new Date());
		for (TestCase tc : config.getTestCases()) {
			if (config.getTrialsCount() > 0) {
				double averageDuration = 0;
				TimerResult finalResult = new TimerResult();
				for (int i=0;i<config.getTrialsCount();i++) {
					TimerResult tempResult = timer.time(tc);
					averageDuration += tempResult.getDuration();
					finalResult.setSize(tempResult.getSize());
					finalResult.setTestCase(tc);
					finalResult.setStartTime(tempResult.getStartTime());
				}
				averageDuration /= config.getTrialsCount();
				finalResult.setDuration((long)averageDuration);
				handleTimerResult(finalResult);
			}
		}
	}
	
	public void handleTimerResult(TimerResult res) {
		System.out.println(res);
	}
}
