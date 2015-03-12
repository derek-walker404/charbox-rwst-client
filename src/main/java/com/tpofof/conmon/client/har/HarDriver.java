package com.tpofof.conmon.client.har;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class HarDriver {
	private WebDriver driver;
	
	public HarDriver() {
		this.driver = getDriver();
        try {
            // Wait till Firebug is loaded
            Thread.sleep(5000);
        } catch (InterruptedException err) {
            System.out.println(err);
        }
	}

	public void run(String url) {
        try {
            // Load test page
            driver.get(url);

            // Wait till HAR is exported
            Thread.sleep(10000);
        }
        catch (InterruptedException err)
        {
            System.out.println(err);
        }
	}
	
	private WebDriver getDriver() {
		if (driver == null) {
			driver = new FirefoxDriver(getProfile());
		}
		return driver;
	}
	
	private FirefoxProfile getProfile() {
		FirefoxProfile profile = new FirefoxProfile();

		File firebug = new File("lib/firebug-2.0.8-fx.xpi");
        File netExport = new File("lib/netExport-0.9b7.xpi");

        try
        {
            profile.addExtension(firebug);
            profile.addExtension(netExport);
        }
        catch (IOException err)
        {
            System.out.println(err);
        }

        // Set default Firefox preferences
        profile.setPreference("app.update.enabled", false);

        String domain = "extensions.firebug.";

        // Set default Firebug preferences
        profile.setPreference(domain + "currentVersion", "2.0.8");
        profile.setPreference(domain + "allPagesActivation", "on");
        profile.setPreference(domain + "defaultPanelName", "net");
        profile.setPreference(domain + "net.enableSites", true);

        // Set default NetExport preferences
        profile.setPreference(domain + "netexport.alwaysEnableAutoExport", true);
        profile.setPreference(domain + "netexport.showPreview", false);
        profile.setPreference(domain + "netexport.defaultLogDir", "/Users/david/Workspace/java/conmon-client/data/har/");
        return profile;
	}
}
