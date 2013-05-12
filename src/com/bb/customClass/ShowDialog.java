package com.bb.customClass;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Status;

import com.bb.controller.Controller;

public class ShowDialog {

	public static ShowDialog INSTANCE = new ShowDialog();

	public ShowDialog() {

	}

	public void dialog(final String message) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				if (DeviceInfo.isSimulator()) {
					Screen screen = UiApplication.getUiApplication().getActiveScreen();
					UiApplication.getUiApplication().popScreen(screen);

					Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
					if (screen2 instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen2);
					}
					Controller.showScreen(new CustomTransperentDialog(message));
				} else {
					Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
					if (screen2 instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen2);
					}
					Controller.showScreen(new CustomTransperentDialog(message));
				}
			}
		});
	}

	public void status(final String message) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				Screen screen = UiApplication.getUiApplication().getActiveScreen();
				if (screen instanceof PopupSpinnerScreen) {
					UiApplication.getUiApplication().popScreen(screen);
				}
				Status.show(message);
			}
		});
	}
}
