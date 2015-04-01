package co.charbox.rwsp.timer.results;

import co.charbox.core.utils.JsonUtils;
import co.charbox.domain.model.TimerResult;

public class ConsoleReporterResultHandler extends TimerResultHandler {

	@Override
	public void handle(TimerResult result) {
		System.out.println(JsonUtils.toJson(result));
	}

}
