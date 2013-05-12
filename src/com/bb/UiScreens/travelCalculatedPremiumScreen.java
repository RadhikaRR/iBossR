package com.bb.UiScreens;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.controller.MainScreenClass;
import com.bb.customClass.PopupSpinnerScreen;
import com.bb.customFields.BorderLabelFieldImpl;
import com.bb.webService.CallWebService;

public class travelCalculatedPremiumScreen extends MainScreenClass {

	GridFieldManager grid1;
	BorderLabelFieldImpl borderLabelFieldImpl;

	public travelCalculatedPremiumScreen(String[] premiumName, String[] premiumValue, final String familyYN,
			final String pParamList) {

		VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL);
		GridTable(premiumName, premiumValue);

		vfm.add(grid1);

		ButtonField btn = new ButtonField("Generate Reference", FIELD_HCENTER);
		btn.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				System.out.println("");
				if (familyYN.equals("Y")) {
					Controller.showScreen(new PopupSpinnerScreen("Please wait..."));
					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;
							flag = CallWebService.INSTANCE.travalGenerateReferenceWS(Constants.IMEI, "xyz",
									FirstScreen.pUpdatedBy, pParamList, FamilyMemberScreen.text);
							while (flag == false) {
								flag = CallWebService.INSTANCE.travalGenerateReferenceWS(Constants.IMEI, "xyz",
										FirstScreen.pUpdatedBy, pParamList, FamilyMemberScreen.text);
							}
						}
					};
					thread.start();
				} else {
					Controller.showScreen(new PopupSpinnerScreen("Please wait..."));
					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;
							flag = CallWebService.INSTANCE.travalGenerateReferenceWS(Constants.IMEI, "xyz",
									FirstScreen.pUpdatedBy, pParamList, "");
							while (flag == false) {
								flag = CallWebService.INSTANCE.travalGenerateReferenceWS(Constants.IMEI, "xyz",
										FirstScreen.pUpdatedBy, pParamList, "");
							}
						}
					};
					thread.start();
				}
				

				// UiApplication.getUiApplication().invokeLater(new Runnable() {
				// public void run() {
				// UiApplication ui = UiApplication.getUiApplication();
				// int screenCount = ui.getScreenCount();
				// for (int i = 0; i < screenCount; i++) {
				// Screen activeScreen = ui.getActiveScreen();
				// if (activeScreen instanceof FirstScreen) {
				// break;
				// } else {
				// ui.popScreen(activeScreen);
				// }
				// }
				// Controller.showScreen(new
				// FirstScreen(Constants.hashtavleFirstScreen));
				// }
				// });
			}
		});

		vfm.add(btn);

		mainVFM.add(vfm);
	}

	public void GridTable(String[] premiumName, String[] premiumValue) {

		grid1 = new GridFieldManager(5, 2, GridFieldManager.FIXED_SIZE);
		grid1.setColumnProperty(0, GridFieldManager.FIXED_SIZE, Display.getWidth() - (Display.getWidth() / 4));
		grid1.setMargin(10, 0, 0, 10);

		borderLabelFieldImpl = new BorderLabelFieldImpl("NAME OF PREMIUM", Field.FOCUSABLE | USE_ALL_WIDTH);
		borderLabelFieldImpl.setFont(Constants.fontBold6);
		borderLabelFieldImpl.setFontColor(Color.WHITE);
		borderLabelFieldImpl.setBgColor(0x000072BC);
		grid1.add(borderLabelFieldImpl);

		borderLabelFieldImpl = new BorderLabelFieldImpl("VALUE", Field.FOCUSABLE | USE_ALL_WIDTH);
		borderLabelFieldImpl.setFont(Constants.fontBold6);
		borderLabelFieldImpl.setFontColor(Color.WHITE);
		borderLabelFieldImpl.setBgColor(0x000072BC);
		grid1.add(borderLabelFieldImpl);

		for (int i = 0; i < 4; i++) {

			borderLabelFieldImpl = new BorderLabelFieldImpl(premiumName[i], Field.FOCUSABLE | USE_ALL_WIDTH);
			borderLabelFieldImpl.setFont(Constants.font);
			// borderLabelFieldImpl.setFontColor(Color.WHITE);
			// borderLabelFieldImpl.setBgColor(0x000072BC);
			grid1.add(borderLabelFieldImpl);

			borderLabelFieldImpl = new BorderLabelFieldImpl(premiumValue[i], Field.FOCUSABLE | USE_ALL_WIDTH);
			borderLabelFieldImpl.setFont(Constants.font);
			// borderLabelFieldImpl.setFontColor(Color.WHITE);
			// borderLabelFieldImpl.setBgColor(0x000072BC);
			grid1.add(borderLabelFieldImpl);
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}

	public boolean onClose() {
		// TODO Auto-generated method stub
		return true;
	}
}
