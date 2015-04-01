package co.charbox.rwsp.timer.results;

import java.util.List;

import co.charbox.domain.model.TimerResult;

import com.google.common.collect.Lists;

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
