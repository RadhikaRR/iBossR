package com.bb.UiScreens;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.controller.MainScreenClass;
import com.bb.customClass.CustomTransperentDialog;
import com.bb.customClass.PopupSpinnerScreen;
import com.bb.customClass.SelectDate;
import com.bb.webService.CallWebService;

public class CalculateIDVScreen extends MainScreenClass {

	public static ObjectChoiceField carMake, carmodel, cartype, businessType;

	LabelField dateLabel, pinCode, IDVLabel, QuoteReRefNo, QuoteReRefNoIs;

	EditField pinEdit, idvEdit;

	String busiType;

	public static LabelField showTodaysDate;

	public static String getMake, VehicleModel, vehicleSubType;
	boolean flag1 = false;
	boolean flag2 = false;
	boolean flag3 = false;

	public static String pinCodeNo, Dt;

	boolean businessFlag = true;

	public CalculateIDVScreen() {

		VerticalFieldManager vfm = new VerticalFieldManager();

		HorizontalFieldManager hfmCar = new HorizontalFieldManager();
		hfmCar.setMargin(10, 0, 0, 0);
		hfmCar.setBorder(roundedBorder);
		carMake = new ObjectChoiceField("Car Make", Constants.carModel);
		// carMake.setPadding(0, 0, 0, 10);
		if (Constants.carModel == null || Constants.carModel.length == 0) {
			String[] r = { "Select" };
			carMake.setChoices(r);
		}
		hfmCar.add(carMake);

		HorizontalFieldManager hfmCarModel = new HorizontalFieldManager();
		hfmCarModel.setBorder(roundedBorder);
		carmodel = new ObjectChoiceField("Car Model", Constants.makeCode);
		// carmodel.setPadding(0, 0, 0, 10);
		if (Constants.makeCode == null || Constants.makeCode.length == 0) {
			String[] r = { "Select" };
			carmodel.setChoices(r);
		} else {
			carmodel.setChoices(Constants.makeCode);
		}
		hfmCarModel.add(carmodel);

		HorizontalFieldManager hfmCarType = new HorizontalFieldManager();
		hfmCarType.setBorder(roundedBorder);
		cartype = new ObjectChoiceField("Car Sub-Type", Constants.VehicleSubName);
		// cartype.setPadding(0, 0, 0, 10);
		if (Constants.VehicleSubName == null || Constants.VehicleSubName.length == 0) {
			String[] r = { "Select" };
			cartype.setChoices(r);
		}
		hfmCarType.add(cartype);

		FieldChangeListener listener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field instanceof ObjectChoiceField) {
					if (field == carMake) {
						int neumericIndex = carMake.getSelectedIndex();
						if (neumericIndex == 0) {
							flag1 = false;
						} else {
							flag1 = true;
							getMake = Constants.carMake[neumericIndex];
							Controller.showScreen(new PopupSpinnerScreen("Processing..."));
							Thread thread = new Thread() {
								public void run() {
									boolean flag = true;
									flag = CallWebService.INSTANCE.getVehicleModelWS(Constants.IMEI, "22", getMake);
									while (flag == false) {
										flag = CallWebService.INSTANCE.getVehicleModelWS(Constants.IMEI, "22", getMake);
									}
									System.out.println("");
								}
							};
							thread.start();
							System.out.println("");
						}
						invalidate();
					} else if (field == carmodel) {
						int index = carmodel.getSelectedIndex();

						if (index == 0) {
							flag2 = false;
						} else {
							flag2 = true;
							VehicleModel = Constants.typeCode[index];
							Controller.showScreen(new PopupSpinnerScreen("Processing..."));
							Thread thread = new Thread() {
								public void run() {
									boolean flag = true;
									flag = CallWebService.INSTANCE.getSubVehicleWS(Constants.IMEI, "22", getMake,
											VehicleModel);
									while (flag == false) {
										flag = CallWebService.INSTANCE.getSubVehicleWS(Constants.IMEI, "22", getMake,
												VehicleModel);
									}
								}
							};
							thread.start();
							System.out.println("");
						}
						invalidate();

					} else if (field == cartype) {
						int ind = cartype.getSelectedIndex();
						if (ind == 0) {
							flag3 = false;
						} else {
							flag3 = true;
							vehicleSubType = Constants.VehicleSubType[ind];
						}
					}
				}
			}
		};
		carMake.setChangeListener(listener);
		carmodel.setChangeListener(listener);
		cartype.setChangeListener(listener);

		System.out.println("");

		HorizontalFieldManager hfmDate = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		hfmDate.setBorder(roundedBorder);
		// hfmDate.setMargin(10, 10, 0, 10);
		dateLabel = new LabelField("Date of Registration");
		showTodaysDate = new LabelField(Constants.curDateMonth);
		if (Display.getWidth() == 480) {
			showTodaysDate.setMargin(0, 0, 0, 100);
		} else {
			showTodaysDate.setMargin(0, 0, 0, 50);
		}
		showTodaysDate.setFont(Constants.fontBold5);
		hfmDate.add(dateLabel);
		hfmDate.add(showTodaysDate);

		final ButtonField btnDt = new ButtonField("Date", FIELD_RIGHT);
		btnDt.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == btnDt) {
					SelectDate.showDatePicker(0);
				}
			}
		});
		btnDt.setMargin(0, 10, 10, 10);

		HorizontalFieldManager hfmPin = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		hfmPin.setBorder(roundedBorder);
		// hfmPin.setMargin(0, 0, 0, 10);
		pinCode = new LabelField("PinCode");
		pinEdit = new EditField("", "", 6, FIELD_HCENTER | EditField.FILTER_NUMERIC) {

			protected void paintBackground(Graphics g) {
				g.clear();

				if (isFocus()) {
					g.setColor(0x000072BC);
					invalidate();
				} else {
					g.setColor(Color.BLACK);
				}
				g.drawRect(0, 0, getWidth(), getHeight());
			}

			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		if (Display.getWidth() == 480) {
			pinEdit.setMargin(0, 13, 0, 200);
		} else {
			pinEdit.setMargin(0, 5, 0, 150);
		}

		hfmPin.add(pinCode);
		hfmPin.add(pinEdit);

		HorizontalFieldManager hfmBusinessType = new HorizontalFieldManager();
		hfmBusinessType.setBorder(roundedBorder);
		String[] bussType = { "Select", "New Business", "Other Company Renewal" };
		businessType = new ObjectChoiceField("Business Type", bussType);
		hfmBusinessType.add(businessType);
		businessType.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field instanceof ObjectChoiceField) {
					if (field == businessType) {
						int ind = businessType.getSelectedIndex();
						switch (ind) {
						case 0:
							businessFlag = true;
							break;
						case 1:
							businessFlag = false;
							busiType = "NB";
							break;
						case 2:
							businessFlag = false;
							busiType = "OTRNW";
							break;
						}
					}
				}
			}
		});

		ButtonField btnCalIDV = new ButtonField("Calculate IDV", FIELD_HCENTER);
		btnCalIDV.setMargin(10, 0, 10, 0);
		btnCalIDV.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				pinCodeNo = pinEdit.getText();
				Dt = showTodaysDate.getText();
				if (flag1 == false) {
					Controller.showScreen(new CustomTransperentDialog("Please select Car Make"));
				} else if (flag2 == false) {
					Controller.showScreen(new CustomTransperentDialog("Please select Car Model"));
				} else if (flag3 == false) {
					Controller.showScreen(new CustomTransperentDialog("Please select Car Sub-Type"));
				} else if (pinCodeNo == null || pinCodeNo.equalsIgnoreCase("") || pinCodeNo.length() < 6) {
					Controller.showScreen(new CustomTransperentDialog("Please enter Proper Pincode"));
				} else if (businessFlag == true) {
					Controller.showScreen(new CustomTransperentDialog("Please select Business Type"));
				} else {
					Controller.showScreen(new PopupSpinnerScreen("Processing..."));
					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;
							flag = CallWebService.INSTANCE.VehicleSubTypeWS(Constants.IMEI, "22", getMake,
									VehicleModel, vehicleSubType, Dt, pinCodeNo, busiType, FirstScreen.pUpdatedBy);
							while (flag == false) {
								flag = CallWebService.INSTANCE.VehicleSubTypeWS(Constants.IMEI, "22", getMake,
										VehicleModel, vehicleSubType, Dt, pinCodeNo, busiType, FirstScreen.pUpdatedBy);
							}
						}
					};
					thread.start();
				}
			}
		});

		vfm.add(hfmCar);
		vfm.add(hfmCarModel);
		vfm.add(hfmCarType);

		vfm.add(hfmDate);
		vfm.add(btnDt);
		vfm.add(hfmPin);
		vfm.add(hfmBusinessType);
		vfm.add(btnCalIDV);

		mainVFM.add(vfm);
	}

	public void popAll() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication ui = UiApplication.getUiApplication();
				int screenCount = ui.getScreenCount();
				for (int i = 0; i < screenCount; i++) {
					Screen activeScreen = ui.getActiveScreen();
					if (activeScreen instanceof StartupScreen) {
						break;
					} else {
						ui.popScreen(activeScreen);
					}
				}
			}
		});
	}

	public boolean onClose() {
		popAll();
		return super.onClose();
	}

	public boolean onSavePrompt() {
		return true;
	}
}
