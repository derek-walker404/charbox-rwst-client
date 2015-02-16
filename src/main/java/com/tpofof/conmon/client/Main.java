package com.tpofof.conmon.client;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.tpofof.conmon.client.timer.TestCaseRunner;

public class Main {
	
	public static void main(String[] args) {
		ConmonConfig config = ConmonConfig.getDefaultConfig();
		Trigger testCaseRunnerTrigger = TriggerBuilder.newTrigger()
				.withIdentity("testCaseRunnerTrigger", "testing")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInMinutes(config.getTestInterval())
						.repeatForever())
				.build();
		JobDetail job = JobBuilder.newJob(TestCaseRunner.class)
				.withIdentity("testCaseRunner", "testing")
				.build();
		
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, testCaseRunnerTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
