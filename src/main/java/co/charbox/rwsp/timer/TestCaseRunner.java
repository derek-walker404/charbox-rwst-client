package co.charbox.rwsp.timer;

import static co.charbox.rwsp.ApplicationSettings.DEVICE_ID_KEY;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import co.charbox.core.utils.Config;
import co.charbox.domain.model.DeviceConfiguration;
import co.charbox.domain.model.TestCase;
import co.charbox.domain.model.TimerResult;
import co.charbox.rwsp.ClientIpProvider;
import co.charbox.rwsp.timer.results.TimerResultHandler;

import com.google.common.collect.Lists;

public class TestCaseRunner {
	
	private final GetAssetTimer getTimer = new GetAssetTimer();
	private final HeadAssetTimer headTimer = new HeadAssetTimer();
	private final List<TimerResultHandler> resultHandlers = TimerResultHandler.getAll();

	public void run(DeviceConfiguration deviceConfig, TestCase tc) {
		if (deviceConfig.getTrialsCount() > 0) {
			TimerResult finalResult = runTrials(tc, getTimer, deviceConfig.getTrialsCount());
			finalResult.setOutage(finalResult.isOutage() || finalResult.getSize() <= 0);
			if (!finalResult.isOutage()) {
				TimerResult pingResults = runTrials(tc, headTimer, deviceConfig.getTrialsCount());
				if (pingResults.isOutage()) {
					finalResult.setOutage(true);
				} else {
					finalResult.setPingDuration(pingResults.getDuration());
				}
			}
			finalResult.getServerLocation().setIp(getIp(tc.getUri()));
			finalResult.setDeviceId(Config.get().getInt(DEVICE_ID_KEY));
			finalResult.setSpeed(calcSpeed(finalResult));
			finalResult.getClientLocation().setIp(ClientIpProvider.getIp());
			for (TimerResultHandler handler : resultHandlers) {
				handler.handle(finalResult);
			}
		}
	}
	
	private double calcSpeed(TimerResult result) {
		double speed = (result.isOutage() || result.getDuration() <= 0) 
				? -1 
				: (result.getSize() * 8.0 / 1048.576 / result.getDuration()); // do I need to multiple by 8 here? math don't work now. comm bak laytur, da nomburs luk gud doh
		return speed;
	}
	
	private String getIp(String assetLocation) {
		String ip = null;
		try {
			ip = InetAddress.getByName(new URL(assetLocation).getHost()).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ip;
	}
	
	private TimerResult runTrials(TestCase tc, AssetTimer<?> timer, int trialsCount) {
		List<Long> durations = Lists.newArrayList();
		TimerResult finalResult = new TimerResult().setTestCaseId(tc.get_id());
		for (int i=0;i<trialsCount;i++) {
			TimerResult tempResult = timer.time(tc);
			// TODO: check that result is valid ensuring that all data was downloaded
			if (!tempResult.isOutage()) {				
				durations.add(tempResult.getDuration());
				finalResult.setSize(tempResult.getSize());
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
}
