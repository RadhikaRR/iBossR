package com.bb.customClass;

import java.io.IOException;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.bb.connection.ConnectionClass;
import com.bb.constants.Constants;

public class AppUpdater {

	public static AppUpdater INSTANCE = new AppUpdater();

	public boolean doCheckForUpdates(String newVersion) {

		if (newVersion != null) {
			String oldVersion = getAppVersion();
			if (!newVersion.equalsIgnoreCase(oldVersion)) {
				return true;
			}
		}
		return false;
	}

	public String getAppVersion() {
		String version = ApplicationDescriptor.currentApplicationDescriptor().getVersion();
		return version;
	}

	String newURL = "";
	int counter = 0;

	public boolean openDownloadURL(String downloadFullURL) {
		try {
			if (Constants.connectTCP) {
				newURL = ConnectionClass.INSTANCE.connectTCP(downloadFullURL);
			} else if (Constants.connectBIS) {
				newURL = ConnectionClass.INSTANCE.connectBIS(downloadFullURL);
			}

			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert("new version is available to download,please download");
					Browser.getDefaultSession().displayPage(newURL);
					System.exit(0);
				}
			});
		} catch (final IOException ioException) {
			LogEventClass.logErrorEvent("Error in Authentication:" + ioException.getMessage());
			Constants.connectTCP = true;
			Constants.connectBIS = false;
			counter = 0;
			ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
		} catch (Exception e) {
			LogEventClass.logErrorEvent("Error in Authentication:" + e.getMessage());
			counter = counter + 1;
			if (counter <= 2) {
				while (counter == 2) {
					Constants.connectBIS = true;
					Constants.connectTCP = false;
					break;
				}
				return false;
			} else {
				counter = 0;
				Constants.connectBIS = false;
				Constants.connectTCP = true;
				ShowDialog.INSTANCE.dialog("Authentication Failed! Please check connection");
			}
		}
		return true;
	}
}
