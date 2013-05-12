package com.bb.controller;

import java.util.Date;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

import com.bb.Startup.SplashScreen;
import com.bb.constants.Constants;
import com.bb.customClass.PopupSpinnerScreen;
import com.bb.persistDB.PersistProvider;
import com.bb.sms.MeSMSSender;
import com.bb.webService.CallWebService;

public class Controller extends UiApplication {

	private String formatted_date;

	public Controller() {
		showScreen(new SplashScreen());
		invokeLater(runnable);
	}

	Runnable runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String smsFlag = PersistProvider.INSTANCE.getObject(Constants.SMSFLAG);
			if (smsFlag == null) {
				Date dt = new Date();
				long date1 = dt.getTime() + (1000 * 60 * 15);
				SimpleDateFormat df = new SimpleDateFormat("h:mm aa");
				formatted_date = df.format(new Date(date1));

				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						MeSMSSender.meSMSSender.sendSMSMessage("09773500500");
						Status.show("Registering! please wait",4000);
						Dialog.alert("Registered Successfully, please reopen application after " + formatted_date);
						System.exit(0);
					}
				});
				
			} else {
				Controller.showScreen(new PopupSpinnerScreen("Authenticating User.."));
				Thread thread = new Thread() {
					public void run() {
						boolean flag = true;
						flag = CallWebService.INSTANCE.authenticateWS(Constants.IMEI);
						while (flag == false) {
							flag = CallWebService.INSTANCE.authenticateWS(Constants.IMEI);
						}
					}
				};
				thread.start();
			}
		}
	};

	public static void main(String[] args) {
		Controller controller = new Controller();
		EventLogger.register(Constants.GUID, Constants.APP_NAME, EventLogger.VIEWER_STRING);
		controller.enterEventDispatcher();
	}

	public static void showScreen(Screen screen) {
		if (screen == null) {
			return;
		}
		synchronized (getEventLock()) {
			getUiApplication().pushScreen(screen);
		}
	}
}
