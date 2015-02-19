package com.tpofof.conmon.client.timer.results;

import com.pofof.conmon.model.TimerResult;
import com.tpofof.utils.JsonUtils;

public class ConsoleReporterResultHandler extends TimerResultHandler {

	@Override
	public void handle(TimerResult result) {
		System.out.println(JsonUtils.toJson(result));
	}

}
