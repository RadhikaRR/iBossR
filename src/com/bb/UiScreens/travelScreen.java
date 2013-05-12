package com.bb.UiScreens;

import java.util.Date;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
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
import com.bb.customFields.BorderLabelFieldImpl;
import com.bb.webService.CallWebService;

public class travelScreen extends MainScreenClass {

	public static ObjectChoiceField PlanName, Area;

	public static LabelField showTodaysDate, showReturnDate, dobEdit;

	public static String stPlanName, stArea;

	public static EditField familyMemberEdit, nameEdit;

	boolean planFlag = true;
	boolean areaFlag = true;

	long days;

	int selectedIndex;

	public static LabelField journeyDaysLabel2;

	public static VerticalFieldManager vfm, vfmToDate;

	public static HorizontalFieldManager hfmJourneyDays, hfmToDate, hfmfamilyMember;

	public static ButtonField btnToDt, btnTravelPremium,btnPremium;

	public travelScreen() {

		vfm = new VerticalFieldManager(VERTICAL_SCROLL);

		VerticalFieldManager vfmName = new VerticalFieldManager(VERTICAL_SCROLL);
		vfmName.setBorder(roundedBorder);
		BorderLabelFieldImpl b1 = new BorderLabelFieldImpl("Enter Name", USE_ALL_WIDTH);
		b1.setFont(Constants.fontBold6);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(0x000072BC);
		// b1.setMargin(5, 10, 0, 10);
		nameEdit = new EditField("", "", 15, FIELD_HCENTER | EDITABLE) {
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
		// nameEdit.setMargin(0, 10, 0, 10);
		vfmName.add(b1);
		vfmName.add(nameEdit);
		vfm.add(vfmName);

		HorizontalFieldManager dobHfm = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		dobHfm.setBorder(roundedBorder);
		dobHfm.setMargin(10, 0, 10, 0);
		LabelField dobLabel = new LabelField("Date Of Birth");
		dobEdit = new LabelField(Constants.curDateMonth);
		dobEdit.setMargin(0, 0, 0, 100);
		dobHfm.add(dobLabel);
		dobHfm.add(dobEdit);
		vfm.add(dobHfm);

		ButtonField btnDOB = new ButtonField("Birth Date", FIELD_RIGHT) {
			protected boolean navigationClick(int status, int time) {
				SelectDate.showDatePicker(2);
				return true;
			}
		};
		btnDOB.setMargin(0, 10, 0, 0);
		vfm.add(btnDOB);

		HorizontalFieldManager planNameHfm = new HorizontalFieldManager();
		planNameHfm.setBorder(roundedBorder);
		PlanName = new ObjectChoiceField("Select Plan", Constants.planName);
		// PlanName.setPadding(10, 10, 0, 10);
		if (Constants.planName == null || Constants.planName.length == 0) {
			String[] r = { "Select" };
			PlanName.setChoices(r);
		}
		planNameHfm.add(PlanName);

		HorizontalFieldManager areaHfm = new HorizontalFieldManager();
		areaHfm.setBorder(roundedBorder);
		Area = new ObjectChoiceField("Select Area", Constants.areaName);
		// Area.setPadding(10, 10, 0, 10);
		if (Constants.areaName == null || Constants.areaName.length == 0) {
			String[] r = { "Select" };
			Area.setChoices(r);
		} else {
			Area.setChoices(Constants.areaName);
		}
		areaHfm.add(Area);

		FieldChangeListener listener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field instanceof ObjectChoiceField) {
					if (field == PlanName) {
						int index = PlanName.getSelectedIndex();
						if (index == 0) {
							planFlag = true;
						} else {
							planFlag = false;
							stPlanName = Constants.planName[index];

							Controller.showScreen(new PopupSpinnerScreen("Please wait..."));
							Thread thread = new Thread() {
								public void run() {
									boolean flag = true;
									flag = CallWebService.INSTANCE.travelAreaWS(Constants.IMEI, FirstScreen.pUpdatedBy,
											stPlanName);
									while (flag == false) {
										flag = CallWebService.INSTANCE.travelAreaWS(Constants.IMEI,
												FirstScreen.pUpdatedBy, stPlanName);
									}
								}
							};
							thread.start();
						}
						invalidate();
					} else {
						int index = Area.getSelectedIndex();
						selectedIndex = index - 1;
						if (index == 0) {
							areaFlag = true;
						} else {
							areaFlag = false;
							stArea = Constants.areaName[index];
						}
						invalidate();
					}
				}
			}
		};
		PlanName.setChangeListener(listener);
		Area.setChangeListener(listener);

		HorizontalFieldManager hfmDate = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		hfmDate.setBorder(roundedBorder);
		// hfmDate.setMargin(20, 10, 0, 10);
		LabelField dateLabel = new LabelField("Journey Date");
		showTodaysDate = new LabelField(Constants.curDateMonth);
		Constants.travelDate = new Date(System.currentTimeMillis());
		if (Display.getWidth() == 480) {
			showTodaysDate.setMargin(0, 0, 0, 130);
		} else {
			showTodaysDate.setMargin(0, 0, 0, 70);
		}
		showTodaysDate.setFont(Constants.fontBold5);
		hfmDate.add(dateLabel);
		hfmDate.add(showTodaysDate);

		final ButtonField btnDt = new ButtonField("Date", FIELD_RIGHT);
		btnDt.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == btnDt) {
					SelectDate.showDatePicker(1);
				}
			}
		});
		btnDt.setMargin(0, 10, 10, 10);

		vfmToDate = new VerticalFieldManager();
		hfmToDate = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		hfmToDate.setBorder(roundedBorder);
		// hfmToDate.setMargin(20, 10, 0, 10);
		LabelField toDateLabel = new LabelField("Return Date :");
		showReturnDate = new LabelField(Constants.curDateMonth);
		Constants.returnDate = new Date(System.currentTimeMillis());
		if (Display.getWidth() == 480) {
			showReturnDate.setMargin(0, 0, 0, 130);
		} else {
			showReturnDate.setMargin(0, 0, 0, 70);
		}
		showReturnDate.setFont(Constants.fontBold5);
		hfmToDate.add(toDateLabel);
		hfmToDate.add(showReturnDate);

		btnToDt = new ButtonField("Date", FIELD_RIGHT);
		btnToDt.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == btnToDt) {
					SelectDate.showReturnDatePicker();
				}
			}
		});
		btnToDt.setMargin(0, 10, 10, 10);

		vfmToDate.add(hfmToDate);
//		if (Constants.journyDays.equals("0")) {
//			vfmToDate.add(btnToDt);
//		}

		// days = daysBetween(Constants.returnDate, Constants.travelDate);
		// System.out.println("------------------days are " + days);

		hfmJourneyDays = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		hfmJourneyDays.setBorder(roundedBorder);
		// hfmJourneyDays.setMargin(20, 10, 0, 10);
		LabelField journeyDaysLabel = new LabelField("No. of Journey Days: ");
		journeyDaysLabel2 = new LabelField("" + days);
		// if(Constants.journyDays.equals("0")){
		// journeyDaysLabel2 = new LabelField("" + days);
		// }else{
		// journeyDaysLabel2 = new LabelField(Constants.journyDays);
		// }

		// journeyDaysLabel2.setMargin(0, 0, 0, 100);
		// if (days == 0L) {
		// journeyDaysLabel2.setText("0");
		// } else {
		// journeyDaysLabel2.setText("" + days);
		// }
		hfmJourneyDays.add(journeyDaysLabel);
		hfmJourneyDays.add(journeyDaysLabel2);

		hfmfamilyMember = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		hfmfamilyMember.setBorder(roundedBorder);
		hfmfamilyMember.setMargin(10, 0, 10, 0);
		LabelField hfmfamilyMemberLabel = new LabelField("Family member");
		familyMemberEdit = new EditField("", "", 4, FIELD_HCENTER | EditField.FILTER_NUMERIC) {
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
			familyMemberEdit.setMargin(0, 13, 0, 200);
		} else {
			familyMemberEdit.setMargin(0, 5, 0, 150);
		}
		hfmfamilyMember.add(hfmfamilyMemberLabel);
		hfmfamilyMember.add(familyMemberEdit);

		btnTravelPremium = new ButtonField("Add family members", FIELD_HCENTER);
		btnTravelPremium.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {

				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {

						String members = familyMemberEdit.getText();
						days = daysBetween(Constants.returnDate, Constants.travelDate);
						System.out.println("------------------days are " + days);

						if (nameEdit.getText().equals("") || nameEdit.getText().equals(null)) {
							Controller.showScreen(new CustomTransperentDialog("Please enter name"));
						} else if (planFlag) {
							Controller.showScreen(new CustomTransperentDialog("Enter Plan Name"));
						} else if (areaFlag) {
							Controller.showScreen(new CustomTransperentDialog("Enter Area"));
						} else if (members.equals(null) || members.equals("")) {
							Controller.showScreen(new CustomTransperentDialog("please enter no. of family members"));
						} else if (Integer.parseInt(members) > 10 || Integer.parseInt(members) <= 0) {
							Controller.showScreen(new CustomTransperentDialog("Family members must be in between 1 to 10"));
						} else if (Constants.journyDays.equals("0") && days < 0L) {
							Controller.showScreen(new CustomTransperentDialog(
									"Return date must be later date than journey date"));
						} else if (stArea.equals(Constants.minName[selectedIndex])
								&& (Integer.parseInt(journeyDaysLabel2.getText()) < Integer
										.parseInt(Constants.minValue[selectedIndex]))) {
							Controller.showScreen(new CustomTransperentDialog("Journey days must be in between "
									+ Constants.minValue[selectedIndex] + " and " + Constants.maxValue[selectedIndex]));
						} else if (stArea.equals(Constants.minName[selectedIndex])
								&& (Integer.parseInt(journeyDaysLabel2.getText()) > Integer
										.parseInt(Constants.maxValue[selectedIndex]))) {
							Controller.showScreen(new CustomTransperentDialog("Journey days must be in between "
									+ Constants.minValue[selectedIndex] + " and " + Constants.maxValue[selectedIndex]));
						} else {
							int memb = Integer.parseInt(members);
							String familyYN = "Y";
							Controller.showScreen(new FamilyMemberScreen(memb,familyYN));
						}
					}
				});
			}
		});

		vfm.add(planNameHfm);
		vfm.add(areaHfm);
		vfm.add(hfmDate);
		vfm.add(btnDt);
		//vfm.add(hfmToDate);

		vfm.add(vfmToDate);

		vfm.add(hfmJourneyDays);

//		if (Constants.familyFlag.equals("Y")) {
//			vfm.add(hfmfamilyMember);
//			vfm.add(btnTravelPremium);
//
//		} else {
			btnPremium = new ButtonField("Generate Premium", FIELD_HCENTER) {
				protected boolean navigationClick(int status, int time) {

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							final String tpTravelFromDate = showTodaysDate.getText();
							final String tpTravelToDate = showReturnDate.getText();
							
							final String tpFamilyMember = "0";							
							
							final String tpFamilyFlag = Constants.familyFlag;

							final String pFamilyList = "0";

							final String tpLoading = "0";
							final String tpDiscount = "0";
							final String tpSpecialDiscount = "0";
							final String tpCommercialDisc = "0";

							final String tpCommDiscRate = Constants.commDiscRate;

							final String pDateOfBirth = dobEdit.getText();
							final String pPassengerName = nameEdit.getText();

							days = daysBetween(Constants.returnDate, Constants.travelDate);
							System.out.println("------------------days are " + days);

							if (pPassengerName.equals("") || pPassengerName.equals(null)) {
								Controller.showScreen(new CustomTransperentDialog("Please enter name"));
							} else if (planFlag) {
								Controller.showScreen(new CustomTransperentDialog("Enter Plan Name"));
							} else if (areaFlag) {
								Controller.showScreen(new CustomTransperentDialog("Enter Area"));
							} else if (Constants.journyDays.equals("0") && days < 0L) {
								Controller.showScreen(new CustomTransperentDialog(
										"Return date must be later date than journey date"));
							} else if (stArea.equals(Constants.minName[selectedIndex])
									&& (Integer.parseInt(journeyDaysLabel2.getText()) < Integer
											.parseInt(Constants.minValue[selectedIndex]))) {
								Controller.showScreen(new CustomTransperentDialog("Journey days must be in between "
										+ Constants.minValue[selectedIndex] + " and "
										+ Constants.maxValue[selectedIndex]));
							} else if (stArea.equals(Constants.maxName[selectedIndex])
									&& (Integer.parseInt(journeyDaysLabel2.getText()) > Integer
											.parseInt(Constants.maxValue[selectedIndex]))) {
								Controller.showScreen(new CustomTransperentDialog("Journey days must be in between "
										+ Constants.minValue[selectedIndex] + " and "
										+ Constants.maxValue[selectedIndex]));
							} else if(Integer.parseInt(tpFamilyMember)<0){
								Controller.showScreen(new CustomTransperentDialog("Family member cannot be zero"));
							}else {
								System.out.println("");
								final String familyYN = "N";
								Controller.showScreen(new PopupSpinnerScreen("Please wait..."));
								Thread thread = new Thread() {
									public void run() {
										boolean flag = true;
										flag = CallWebService.INSTANCE.travalPremiumWS(Constants.IMEI,
												FirstScreen.pUpdatedBy, stPlanName, stArea, tpTravelFromDate,
												tpTravelToDate, tpFamilyMember, tpFamilyFlag, pFamilyList, tpLoading,
												tpDiscount, tpSpecialDiscount, tpCommercialDisc, tpCommDiscRate,
												pDateOfBirth, pPassengerName,familyYN);
										while (flag == false) {
											flag = CallWebService.INSTANCE.travalPremiumWS(Constants.IMEI,
													FirstScreen.pUpdatedBy, stPlanName, stArea, tpTravelFromDate,
													tpTravelToDate, tpFamilyMember, tpFamilyFlag, pFamilyList,
													tpLoading, tpDiscount, tpSpecialDiscount, tpCommercialDisc,
													tpCommDiscRate, pDateOfBirth, pPassengerName,familyYN);
										}
									}
								};
								thread.start();
							}
						}
					});
					return true;
				}
			};
			btnPremium.setMargin(5, 0, 5, 0);
			//vfm.add(btnPremium);
		//}

			mainVFM.add(vfm);
	}

	public static long daysBetween(Date max, Date min) {
		return (max.getTime() - min.getTime()) / 86400000;
	}

	protected boolean onSavePrompt() {
		return true;
	}
}
