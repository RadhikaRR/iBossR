package com.bb.UiScreens;

import java.util.Hashtable;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.controller.MainScreenClass;
import com.bb.customClass.PopupSpinnerScreen;
import com.bb.customFields.BitmapButtonField;
import com.bb.webService.CallWebService;

public class StartupScreen extends MainScreenClass {

	LabelField labelPremiumCalculator, iBoss;
	ButtonField btnCalculateIDV, btnCalculatePremium;
	BitmapButtonField btnCar;

	public StartupScreen(Hashtable hashtable, int carTravel) {	
		
		VerticalFieldManager vfm = new VerticalFieldManager(USE_ALL_WIDTH);

		iBoss = new LabelField("", FIELD_HCENTER);
		iBoss.setMargin(30, 0, 30, 0);
		iBoss.setFont(Constants.fontBold9);

		Bitmap carUnclick = Bitmap.getBitmapResource("calcUnclick.png");
		Bitmap carClick = Bitmap.getBitmapResource("calcClickedr.png");

		if (carTravel == 0) {
			iBoss.setText("Premium Calculation - iMotor");
			btnCar = new BitmapButtonField(carUnclick, carClick, FIELD_HCENTER) {
				protected boolean navigationClick(int status, int time) {
					Controller.showScreen(new PopupSpinnerScreen("Please wait..."));
					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;
							flag = CallWebService.INSTANCE.getVehicleMakeWS(Constants.IMEI);
							while (flag == false) {
								flag = CallWebService.INSTANCE.getVehicleMakeWS(Constants.IMEI);
							}
						}
					};
					thread.start();
					return true;
				}
			};
		} else {
			iBoss.setText("Premium Calculation - iTravel");
			btnCar = new BitmapButtonField(carUnclick, carClick, FIELD_HCENTER) {
				protected boolean navigationClick(int status, int time) {
					Controller.showScreen(new PopupSpinnerScreen("Please wait..."));
					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;
							flag = CallWebService.INSTANCE.travelPlanWS(Constants.IMEI,FirstScreen.pUpdatedBy);
							while (flag == false) {
								flag = CallWebService.INSTANCE.travelPlanWS(Constants.IMEI,FirstScreen.pUpdatedBy);
							}
						}
					};
					thread.start();
					return true;
				}
			};
		}

		vfm.add(iBoss);
		vfm.add(btnCar);
		
		mainVFM.add(vfm);
	}

	public boolean onClose() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication ui = UiApplication.getUiApplication();
				int screenCount = ui.getScreenCount();
				for (int i = 0; i < screenCount; i++) {
					Screen activeScreen = ui.getActiveScreen();
					if (activeScreen instanceof FirstScreen) {
						break;
					} else {
						ui.popScreen(activeScreen);
					}
				}
			}
		});
		return true;
	}

	public boolean onSavePrompt() {
		return true;
	}
}
