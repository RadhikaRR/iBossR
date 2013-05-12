package com.bb.UiScreens;

import java.util.Hashtable;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.controller.MainScreenClass;
import com.bb.customClass.OkExitCustomTransperentDialog;
import com.bb.customFields.BitmapButtonField;
import com.bb.customFields.LabelFieldImpl;

public class FirstScreen extends MainScreenClass {
	BitmapButtonField btnCar, btnAeroplane;

	Bitmap carUnclick = Bitmap.getBitmapResource("CarUnclick.png");
	Bitmap carClick = Bitmap.getBitmapResource("CarClicked.png");

	Bitmap travelUnclick = Bitmap.getBitmapResource("planeUnClick.png");
	Bitmap travelClick = Bitmap.getBitmapResource("planeClick.png");

	protected static String pUpdatedBy;

	private LabelFieldImpl WelcomeNameLbl;
	private LabelFieldImpl DisplayTimeLbl;

	protected HorizontalFieldManager horizontalWelcomeLabel;
	String userName;

	public FirstScreen(final Hashtable hashtable, String[] moduleVisible) {

		userName = (String) hashtable.get("stringval1");

		welcomeTitleDisplay();

		mainVFM.add(horizontalWelcomeLabel);

		pUpdatedBy = (String) hashtable.get("stringval37");

		HorizontalFieldManager hfm = new HorizontalFieldManager(FIELD_HCENTER);

		VerticalFieldManager vfm1 = new VerticalFieldManager(FIELD_HCENTER);
		VerticalFieldManager vfm2 = new VerticalFieldManager(FIELD_HCENTER);

		vfm1.setMargin(30, 50, 10, 30);
		vfm2.setMargin(30, 30, 10, 50);

		btnCar = new BitmapButtonField(carUnclick, carClick, FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				Controller.showScreen(new StartupScreen(hashtable, 0));
				return true;
			}
		};
		// btnCar.setMargin(30, 0, 0, 30);

		LabelField l2 = new LabelField("iMotor", FIELD_HCENTER);
		// l2.setMargin(10, 0, 0, 0);
		l2.setFont(Constants.fontBold6);

		btnAeroplane = new BitmapButtonField(travelUnclick, travelClick, FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				Controller.showScreen(new StartupScreen(hashtable, 1));
				return true;
			}
		};
		// btnAeroplane.setMargin(30, 0, 0, 30);

		LabelField l3 = new LabelField("iTravel", FIELD_HCENTER);
		// l3.setMargin(10, 0, 0, 0);
		l3.setFont(Constants.fontBold6);

		vfm1.add(btnCar);
		vfm1.add(l2);
		vfm2.add(btnAeroplane);
		vfm2.add(l3);

		for (int i = 0; i < moduleVisible.length; i++) {
			if (moduleVisible[i].equals("11")) {
				Constants.iMotorModuleVisible = true;
			} else if (moduleVisible[i].equals("12")) {
				Constants.iTravelModuleVisible = true;
			}
		}

		if (Constants.iMotorModuleVisible == true) {
			hfm.add(vfm1);
		}
		if (Constants.iTravelModuleVisible == true) {
			hfm.add(vfm2);
		}

		mainVFM.add(hfm);
	}

	public boolean onClose() {
		Controller.showScreen(new OkExitCustomTransperentDialog("Do you want to EXIT?"));
		return true;
	}

	public boolean onSavePrompt() {
		return true;
	}

	public void welcomeTitleDisplay() {

		String curDateForDisplayLbl = new SimpleDateFormat("dd-MMM-yyyy").formatLocal(System.currentTimeMillis());

		DisplayTimeLbl = new LabelFieldImpl(curDateForDisplayLbl, DrawStyle.RIGHT | Field.USE_ALL_WIDTH
				| Field.USE_ALL_HEIGHT);

		DisplayTimeLbl.setBgColor(0x000072BC);
		DisplayTimeLbl.setFontColor(Color.WHITE);

		WelcomeNameLbl = new LabelFieldImpl("Welcome: " + userName);
		WelcomeNameLbl.setBgColor(0x000072BC);
		WelcomeNameLbl.setFontColor(Color.WHITE);

		if (Display.getWidth() > 320) {
			DisplayTimeLbl.setFont(Constants.font);
			WelcomeNameLbl.setFont(Constants.font);
		} else {
			DisplayTimeLbl.setFont(Constants.fontVerySmall);
			WelcomeNameLbl.setFont(Constants.fontVerySmall);
		}

		horizontalWelcomeLabel = new HorizontalFieldManager(HorizontalFieldManager.USE_ALL_WIDTH);
		horizontalWelcomeLabel.add(WelcomeNameLbl);
		horizontalWelcomeLabel.add(DisplayTimeLbl);
		horizontalWelcomeLabel.setBorder(roundedBorder);
	}
}
