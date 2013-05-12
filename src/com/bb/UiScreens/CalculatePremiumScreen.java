package com.bb.UiScreens;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
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
import com.bb.customFields.BorderLabelFieldImpl;
import com.bb.webService.CallWebService;

public class CalculatePremiumScreen extends MainScreenClass {

	LabelField QuoteReRefNo, QuoteReRefNoIs, calculatedIDVLabel, NCBlabel, commDiscLabel, elecAccLabel,
			nonElecAccLabel, cngLabel, passCoverLabel, driveAssuredCovLabel, netPremiumLabel, viewDetailedBreakupLabel,
			QuoteRefNoLabel;
	static EditField calculatedIDVEdit, commDiscEdit, elecAccEdit, nonElecAccEdit, cngEdit, assCoverEdit,
			coverElectEdit, driveAssuredCovEdit, netPremiumEdit;

	BorderLabelFieldImpl b1;
	public static String calculateIdvv;
	public static String getNCB = "0";

	boolean continueFlagNB = false;
	boolean continueFlagOTRNW = false;

	int MAX, MIN;

	CheckboxField driveAssureCov;
	String driveAssureCovString = "N";
	static String pncbValue;

	public CalculatePremiumScreen(String calculateIdv, final String pBussType, final String pUserName,
			final String stringval11, final String stringval12, final String stringval13, final String stringval14) {
		CalculatePremiumScreen.calculateIdvv = calculateIdv;

		VerticalFieldManager mainVfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | VERTICAL_SCROLLBAR);

		HorizontalFieldManager hfmQuote = new HorizontalFieldManager(Field.USE_ALL_WIDTH | FIELD_HCENTER);
		hfmQuote.setMargin(10, 10, 20, 10);
		QuoteReRefNo = new LabelField("Calculated IDV is :");
		QuoteReRefNoIs = new LabelField(" " + calculateIdv);
		hfmQuote.add(QuoteReRefNo);
		hfmQuote.add(QuoteReRefNoIs);
		mainVfm.add(hfmQuote);

		int idv = Integer.parseInt(calculateIdv);

		MAX = (idv + (idv / 20));
		MIN = (idv - (idv / 20));
		System.out.println("");

		b1 = new BorderLabelFieldImpl("Calculated IDV (Rs.)", USE_ALL_WIDTH);
		b1.setFont(Constants.fontBold6);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(0x000072BC);
		b1.setMargin(5, 10, 0, 10);
		calculatedIDVEdit = new EditField("", "", 12, FIELD_HCENTER | EDITABLE | EditField.FILTER_NUMERIC) {
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
		calculatedIDVEdit.setMargin(0, 10, 0, 10);
		calculatedIDVEdit.setText(calculateIdv);
		mainVfm.add(b1);
		mainVfm.add(calculatedIDVEdit);

		b1 = new BorderLabelFieldImpl("Commercial Discount(%)", USE_ALL_WIDTH);
		b1.setFont(Constants.fontBold6);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(0x000072BC);
		b1.setMargin(5, 10, 0, 10);
		commDiscEdit = new EditField("", "0", 3, FIELD_HCENTER | FOCUSABLE | EditField.FILTER_NUMERIC) {
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
		commDiscEdit.setMargin(0, 10, 0, 10);
		mainVfm.add(b1);
		mainVfm.add(commDiscEdit);

		String[] ncbStr = { "0", "20", "25", "35", "45", "50", "55", "65" };
		final ObjectChoiceField NCB = new ObjectChoiceField("NCB at Renewal(%)", ncbStr);
		FieldChangeListener listener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field instanceof ObjectChoiceField) {
					if (field == NCB) {
						int ind = NCB.getSelectedIndex();
						getNCB = (String) NCB.getChoice(ind);
					}
				}
			}
		};
		NCB.setChangeListener(listener);
		NCB.setMargin(10, 10, 10, 10);
		mainVfm.add(NCB);

		b1 = new BorderLabelFieldImpl("Cover Electrical Accessories(Rs.)", USE_ALL_WIDTH);
		b1.setFont(Constants.fontBold6);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(0x000072BC);
		b1.setMargin(5, 10, 0, 10);

		coverElectEdit = new EditField("", "0", 12, FIELD_HCENTER | FOCUSABLE | EditField.FILTER_NUMERIC) {
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
		coverElectEdit.setMargin(0, 10, 0, 10);
		mainVfm.add(b1);
		mainVfm.add(coverElectEdit);

		b1 = new BorderLabelFieldImpl("Cover Non-Electrical Accessories(Rs.)", USE_ALL_WIDTH);
		b1.setFont(Constants.fontBold6);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(0x000072BC);
		b1.setMargin(5, 10, 0, 10);

		nonElecAccEdit = new EditField("", "0", 12, FIELD_HCENTER | FOCUSABLE | EditField.FILTER_NUMERIC) {
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
		nonElecAccEdit.setMargin(0, 10, 0, 10);
		mainVfm.add(b1);
		mainVfm.add(nonElecAccEdit);

		b1 = new BorderLabelFieldImpl("Cover CNG kit(Rs.)", USE_ALL_WIDTH);
		b1.setFont(Constants.fontBold6);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(0x000072BC);
		b1.setMargin(5, 10, 0, 10);

		cngEdit = new EditField("", "0", 12, FIELD_HCENTER | FOCUSABLE | EditField.FILTER_NUMERIC) {
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
		cngEdit.setMargin(0, 10, 0, 10);
		mainVfm.add(b1);
		mainVfm.add(cngEdit);

		b1 = new BorderLabelFieldImpl("Co-passenger cover worth(Rs.)", USE_ALL_WIDTH);
		b1.setFont(Constants.fontBold6);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(0x000072BC);
		b1.setMargin(5, 10, 0, 10);

		assCoverEdit = new EditField("", "0", 12, FIELD_HCENTER | FOCUSABLE | EditField.FILTER_NUMERIC) {
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
		assCoverEdit.setMargin(0, 10, 20, 10);
		mainVfm.add(b1);
		mainVfm.add(assCoverEdit);

		driveAssureCov = new CheckboxField("Drive Assure-Economy", false);
		driveAssureCov.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == driveAssureCov) {
					System.out.println("");
					if (driveAssureCov.getChecked()) {
						driveAssureCovString = "Y";
					} else {
						driveAssureCovString = "N";
					}
				}
			}
		});
		driveAssureCov.setMargin(10, 0, 5, 10);
		mainVfm.add(driveAssureCov);

		final ButtonField btnCalPrem = new ButtonField("Calculate Premium", FIELD_HCENTER);
		btnCalPrem.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == btnCalPrem) {

					// if (calculatedIDVEdit.getText().equals("") ||
					// commDiscEdit.getText().equals("")
					// || coverElectEdit.getText().equals("") ||
					// nonElecAccEdit.getText().equals("")
					// || cngEdit.getText().equals("") ||
					// assCoverEdit.getText().equals("")) {
					// Controller.showScreen(new
					// CustomTransperentDialog("Fields cannot be empty"));
					// } else {

					int commDiscint = Integer.parseInt(commDiscEdit.getText());
					int calculatedIDVint = Integer.parseInt(calculatedIDVEdit.getText());
					int coverElectint = Integer.parseInt(coverElectEdit.getText());
					int nonElecAccint = Integer.parseInt(nonElecAccEdit.getText());
					int cngEditint = Integer.parseInt(cngEdit.getText());
					int assCoverint = Integer.parseInt(assCoverEdit.getText());
					int calIDVint = Integer.parseInt(calculateIdvv);

					if (pBussType.equals("NB")) {
						if (commDiscint > Integer.parseInt(stringval11)) {
							int ask = Dialog
									.ask(
											Dialog.D_YES_NO,
											"You are entering Commercial-Discount more than allocated\n Do you want to continue?",
											Dialog.YES);

							if (ask == Dialog.YES) {
								continueFlagNB = true;
							} else {
								continueFlagNB = false;
							}
						} else {
							continueFlagNB = true;
						}
					} else if (pBussType.equals("OTRNW")) {

						int selectedNCB = Integer.parseInt(getNCB);
						if (selectedNCB == 0) {
							if (commDiscint > Integer.parseInt(stringval12)) {
								int ask = Dialog
										.ask(
												Dialog.D_YES_NO,
												"You are entering Commercial-Discount more than allocated\n Do you want to continue?",
												Dialog.YES);

								if (ask == Dialog.YES) {
									continueFlagNB = true;
								} else {
									continueFlagNB = false;
								}
							} else {
								continueFlagNB = true;
							}
						} else if (selectedNCB > 0 && selectedNCB <= 20) {
							if (commDiscint > Integer.parseInt(stringval13)) {
								int ask = Dialog
										.ask(
												Dialog.D_YES_NO,
												"You are entering Commercial-Discount more than allocated\n Do you want to continue?",
												Dialog.YES);

								if (ask == Dialog.YES) {
									continueFlagNB = true;
								} else {
									continueFlagNB = false;
								}
							} else {
								continueFlagNB = true;
							}
						} else if (selectedNCB >= 25) {
							if (commDiscint > Integer.parseInt(stringval14)) {
								int ask = Dialog
										.ask(
												Dialog.D_YES_NO,
												"You are entering Commercial-Discount more than allocated\n Do you want to continue?",
												Dialog.YES);

								if (ask == Dialog.YES) {
									continueFlagNB = true;
								} else {
									continueFlagNB = false;
								}
							} else {
								continueFlagNB = true;
							}
						}
					}

					if (continueFlagNB == true) {

						if (commDiscint > 100 || commDiscint < 0 || commDiscEdit.getText().equals("")) {
							Controller.showScreen(new CustomTransperentDialog(
									"Enter Commercial Discount less than 100%"));
						} else if (calculatedIDVint > MAX) {
							Controller
									.showScreen(new CustomTransperentDialog("Calculated IDV must be less than " + MAX));
						} else if (calculatedIDVint < MIN) {
							Controller.showScreen(new CustomTransperentDialog("Calculated IDV must be greter than "
									+ MIN));
						} else if (coverElectint > calIDVint) {
							Controller.showScreen(new CustomTransperentDialog("Cover Electrical Acc must be less than "
									+ calculateIdvv));
						} else if (nonElecAccint > calIDVint) {
							Controller.showScreen(new CustomTransperentDialog(
									"Cover Non-Electrical Acc must be less than " + calculateIdvv));
						} else if (cngEditint > calIDVint) {
							Controller.showScreen(new CustomTransperentDialog("Cover CNG kit must be less than "
									+ calculateIdvv));
						} else if (assCoverint > calIDVint) {
							Controller.showScreen(new CustomTransperentDialog(
									"Co-passanger cover worth must be less than " + calculateIdvv));
						} else {

							final String pVecTypeCode = "22";
							final String pVecMakeCode = CalculateIDVScreen.getMake;
							final String pVecModel = CalculateIDVScreen.VehicleModel;
							final String pVecSubType = CalculateIDVScreen.vehicleSubType;
							final String pPinCode = CalculateIDVScreen.pinCodeNo;
							final String pdate = CalculateIDVScreen.Dt;
							final String pVecNonElec = nonElecAccEdit.getText();
							final String pVecElec = coverElectEdit.getText();
							final String pVecCng = cngEdit.getText();
							final String pSumAssured = assCoverEdit.getText();
							final String pCalculatedIDV = String.valueOf(calculatedIDVint);
							final String pcommercialdisc = commDiscEdit.getText();

							if (getNCB.equals("") || getNCB == null || getNCB.equals(null)) {
								pncbValue = "0";
							} else {
								pncbValue = getNCB;
							}

							System.out.println("");
							Controller.showScreen(new PopupSpinnerScreen("Processing..."));
							Thread thread = new Thread() {
								public void run() {
									boolean flag = true;
									flag = CallWebService.INSTANCE.calculatePremiumWS(Constants.IMEI, pVecTypeCode,
											pVecMakeCode, pVecModel, pVecSubType, pPinCode, pdate, pVecNonElec,
											pVecElec, pVecCng, pSumAssured, pCalculatedIDV, pcommercialdisc,
											driveAssureCovString, pncbValue, pBussType, pUserName);
									while (flag == false) {
										flag = CallWebService.INSTANCE.calculatePremiumWS(Constants.IMEI, pVecTypeCode,
												pVecMakeCode, pVecModel, pVecSubType, pPinCode, pdate, pVecNonElec,
												pVecElec, pVecCng, pSumAssured, pCalculatedIDV, pcommercialdisc,
												driveAssureCovString, pncbValue, pBussType, pUserName);
									}
									System.out.println("");
								}
							};
							thread.start();
						}
					}
				}
			}
		});
		mainVfm.add(btnCalPrem);

		mainVFM.add(mainVfm);
	}

	public boolean onClose() {

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication ui = UiApplication.getUiApplication();
				int screenCount = ui.getScreenCount();
				for (int i = 0; i < screenCount; i++) {
					Screen activeScreen = ui.getActiveScreen();
					if (activeScreen instanceof CalculateIDVScreen) {
						break;
					} else {
						ui.popScreen(activeScreen);
					}
				}
			}
		});

		// UiApplication.getUiApplication().invokeLater(new Runnable() {
		// public void run() {
		// if (DeviceInfo.isSimulator()) {
		// Screen screen = UiApplication.getUiApplication().getActiveScreen();
		// if (screen instanceof Dialog) {
		// UiApplication.getUiApplication().popScreen(screen);
		// }
		//
		// Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
		// if (screen2 instanceof CalculatePremiumScreen) {
		// UiApplication.getUiApplication().popScreen(screen2);
		// }
		//
		// Screen screen3 = UiApplication.getUiApplication().getActiveScreen();
		// UiApplication.getUiApplication().popScreen(screen3);
		//
		// Screen screen1 = UiApplication.getUiApplication().getActiveScreen();
		// if (screen1 instanceof PopupSpinnerScreen) {
		// UiApplication.getUiApplication().popScreen(screen1);
		// }
		// } else {
		//
		// Screen screen = UiApplication.getUiApplication().getActiveScreen();
		// if (screen instanceof Dialog) {
		// UiApplication.getUiApplication().popScreen(screen);
		// }
		//
		// Screen screen2 = UiApplication.getUiApplication().getActiveScreen();
		// if (screen2 instanceof CalculatePremiumScreen) {
		// UiApplication.getUiApplication().popScreen(screen2);
		// }
		//
		// Screen screen1 = UiApplication.getUiApplication().getActiveScreen();
		// if (screen1 instanceof PopupSpinnerScreen) {
		// UiApplication.getUiApplication().popScreen(screen1);
		// }
		// }
		// }
		// });
		return super.onClose();
	}

	public boolean onSavePrompt() {
		return true;
	}
}
