package com.tpofof.conmon.client.timer.results;

import java.util.List;

import com.google.common.collect.Lists;
import com.pofof.conmon.model.TimerResult;

public abstract class TimerResultHandler {

	public abstract void handle(TimerResult result);
	
	private static List<TimerResultHandler> handlers;
	
	public static List<TimerResultHandler> getAll() {
		if (handlers == null) {
			handlers = Lists.newArrayList();
			handlers.add(new ConmonPushResultHandler());
			handlers.add(new ConsoleReporterResultHandler());
		}
		return handlers;
	}
}
