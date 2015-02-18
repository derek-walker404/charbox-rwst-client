package com.tpofof.conmon.client;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.pofof.conmon.model.DeviceConfiguration;
import com.tpofof.conmon.client.config.DeviceManager;
import com.tpofof.conmon.client.timer.TestCaseRunner;

public class Main {
	
	public static void main(String[] args) {
		DeviceConfiguration deviceConfig = DeviceManager.getCurrentConfig(true);
		Trigger testCaseRunnerTrigger = TriggerBuilder.newTrigger()
				.withIdentity("testCaseRunnerTrigger", "timers")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInMinutes(deviceConfig.getTestInterval())
						.repeatForever())
				.build();
		JobDetail job = JobBuilder.newJob(TestCaseRunner.class)
				.withIdentity("testCaseRunner", "timers")
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
