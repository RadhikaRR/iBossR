package com.bb.UiScreens;

import java.util.Calendar;
import java.util.Date;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.picker.DateTimePicker;

import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.controller.MainScreenClass;
import com.bb.customClass.CustomTransperentDialog;
import com.bb.customClass.PopupSpinnerScreen;
import com.bb.customFields.BorderLabelFieldImpl;
import com.bb.customFields.StringSplitter;
import com.bb.webService.CallWebService;

public class FamilyMemberScreen extends MainScreenClass {

	private static Date date;
	private static Calendar cal;
	public static String selDate;
	private static boolean isFutureDate;

	static LabelField ll;

	EditField e;
	LabelField l;
	ButtonField b;

	ButtonField bf;

	GridFieldManager grid1;
	BorderLabelFieldImpl label;
	EditField nameEdit;
	ObjectChoiceField relation;
	DateField dob;
	ButtonField btn;
	boolean emptyEdit = false;
	boolean emptyRelation = false;
	
	public static String text = "";

	public FamilyMemberScreen(int members,final String familyY) {

		VerticalFieldManager vfm = new VerticalFieldManager();
		GridTable(members);

		VerticalFieldManager gridHorizontalFieldManager1 = new VerticalFieldManager(HORIZONTAL_SCROLL | VERTICAL_SCROLL);
		gridHorizontalFieldManager1.add(grid1);
		gridHorizontalFieldManager1.setMargin(10, 5, 0, 5);
		vfm.add(gridHorizontalFieldManager1);

		btn = new ButtonField("Generate Premium", FIELD_HCENTER);
		btn.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == btn) {
					
					int fieldCount = grid1.getFieldCount();
					for (int i = 3; i < fieldCount; i++) {
						Field field2 = grid1.getField(i);

						if (field2 instanceof EditField) {
							EditField field3 = (EditField) field2;
							if (field3.getText().equals("") || field3.getText().equals(null)) {
								emptyEdit = true;
								break;
							} else {
								emptyEdit = false;
								text += "#M#" + field3.getText() + "~";
							}
						} else if (field2 instanceof LabelField) {
							//Field f = ((HorizontalFieldManager) field2).getField(0);
							System.out.println("1111------------------------");
							 text += field2 + "~";
							//text += f + "~";
						} else if (field2 instanceof ObjectChoiceField) {
							System.out.println("0000------------------------");
							if (field2.equals("Select")) {
								emptyRelation = true;
								break;
							} else {
								emptyRelation = false;
								text += field2;
							}
						}
					}
					System.out.println("");
					if (emptyEdit == true) {
						Controller.showScreen(new CustomTransperentDialog("please fill the entire field"));
					} else if (emptyRelation == true) {
						Controller.showScreen(new CustomTransperentDialog("please fill the entire field"));
					} else {
						final String tpTravelFromDate = travelScreen.showTodaysDate.getText();
						final String tpTravelToDate = travelScreen.showReturnDate.getText();
						final String tpFamilyMember = travelScreen.familyMemberEdit.getText();
						final String tpFamilyFlag = Constants.familyFlag;

						final String pFamilyList = text;

						final String tpLoading = "0";
						final String tpDiscount = "0";
						final String tpSpecialDiscount = "0";
						final String tpCommercialDisc = "0";

						final String tpCommDiscRate = Constants.commDiscRate;

						final String pDateOfBirth = travelScreen.dobEdit.getText();
						final String pPassengerName = travelScreen.nameEdit.getText();
						System.out.println("");
						Controller.showScreen(new PopupSpinnerScreen("Please wait..."));
						Thread thread = new Thread() {
							public void run() {
								boolean flag = true;
								flag = CallWebService.INSTANCE.travalPremiumWS(Constants.IMEI, FirstScreen.pUpdatedBy,
										travelScreen.stPlanName, travelScreen.stArea, tpTravelFromDate, tpTravelToDate,
										tpFamilyMember, tpFamilyFlag, pFamilyList, tpLoading, tpDiscount, tpSpecialDiscount,
										tpCommercialDisc, tpCommDiscRate, pDateOfBirth, pPassengerName,familyY);
								while (flag == false) {
									flag = CallWebService.INSTANCE.travalPremiumWS(Constants.IMEI,
											FirstScreen.pUpdatedBy, travelScreen.stPlanName, travelScreen.stArea,
											tpTravelFromDate, tpTravelToDate, tpFamilyMember, tpFamilyFlag, pFamilyList,
											tpLoading, tpDiscount, tpSpecialDiscount, tpCommercialDisc, tpCommDiscRate,
											pDateOfBirth, pPassengerName,familyY);
								}
							}
						};
						thread.start();
					}
				}
			}
		});
		btn.setMargin(5, 0, 5, 0);
		vfm.add(btn);

//		FieldChangeListener listener = new FieldChangeListener() {
//			public void fieldChanged(Field field, int context) {
//				if (field instanceof GridFieldManager) {
//					int in = grid1.getFieldWithFocusIndex();
//				}
//			}
//		};

		mainVFM.add(vfm);
	}

	public void GridTable(int members) {

		grid1 = new GridFieldManager(members + 1, 3, GridFieldManager.FIXED_SIZE | GridFieldManager.FOCUSABLE);
		grid1.setColumnProperty(0, GridFieldManager.FIXED_SIZE, Display.getWidth() - (Display.getWidth() / 4));

		label = new BorderLabelFieldImpl("NAME OF FAMILY MEMBERS", USE_ALL_WIDTH | FOCUSABLE);
		label.setFont(Constants.fontBold6);
		label.setFontColor(Color.WHITE);
		label.setBgColor(0x000072BC);
		grid1.add(label);

		label = new BorderLabelFieldImpl("DOB", USE_ALL_WIDTH | FOCUSABLE);
		label.setFont(Constants.fontBold6);
		label.setFontColor(Color.WHITE);
		label.setBgColor(0x000072BC);
		grid1.add(label);

		label = new BorderLabelFieldImpl("RELATION", USE_ALL_WIDTH | FOCUSABLE);
		label.setFont(Constants.fontBold6);
		label.setFontColor(Color.WHITE);
		label.setBgColor(0x000072BC);
		grid1.add(label);

		for (int i = 0; i < members; i++) {

			nameEdit = new EditField("", "", 20, Field.FOCUSABLE) {
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
			grid1.add(nameEdit);

			// HorizontalFieldManager hfm = new
			// HorizontalFieldManager(Manager.FOCUSABLE);
			// SimpleDateFormat dateFormat = new
			// SimpleDateFormat("dd-MMM-yyyy");
			// dob = new DateField("", System.currentTimeMillis(), dateFormat,
			// Field.FOCUSABLE);
			// hfm.add(dob);
			// grid1.add(hfm);

			// HorizontalFieldManager hfm = new HorizontalFieldManager();
			ll = new LabelField(Constants.curDateMonth, Field.FOCUSABLE) {
				protected boolean navigationClick(int status, int time) {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							System.out.println("");
							final DateTimePicker dateTimePicker = DateTimePicker.createInstance(Calendar.getInstance(),
									"yyyy:MM:dd", null);
							dateTimePicker.doModal();

							cal = dateTimePicker.getDateTime();
							date = cal.getTime();

							if (date != null) {
								selDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date(date.getTime()));
								isFutureDate = doCheckFutureDate(selDate, Constants.curDate);
								if (isFutureDate) {
									int in = grid1.getFieldWithFocusIndex();
									Field f = grid1.getField(in);
									if (f instanceof LabelField) {
										((LabelField) f).setText(Constants.curDateMonth);
									}
									Controller
											.showScreen(new CustomTransperentDialog("D.O.B. cannot be a future date"));
								} else {
									String validDateForlbl = new SimpleDateFormat("dd-MMM-yyyy").format(new Date(date
											.getTime()));

									int in = grid1.getFieldWithFocusIndex();
									Field f = grid1.getField(in);
									if (f instanceof LabelField) {
										((LabelField) f).setText(validDateForlbl);
									}
								}
							}
						}
					});
					return true;
				}
			};
			ll.setCookie("" + i);
			// bf = new ButtonField(Field.FOCUSABLE);
			// ll.setChangeListener(new FieldChangeListener() {
			// public void fieldChanged(Field field, int context) {
			// System.out.println("");
			// if (field instanceof LabelField) {
			//						
			// UiApplication.getUiApplication().invokeLater(new Runnable() {
			// public void run() {
			// final DateTimePicker dateTimePicker =
			// DateTimePicker.createInstance(Calendar.getInstance(),
			// "yyyy:MM:dd", null);
			// dateTimePicker.doModal();
			//
			// cal = dateTimePicker.getDateTime();
			// date = cal.getTime();
			//
			// if (date != null) {
			// selDate = new SimpleDateFormat("dd-MM-yyyy").format(new
			// Date(date.getTime()));
			// isFutureDate = doCheckFutureDate(selDate, Constants.curDate);
			// if (isFutureDate) {
			// ll.setText(Constants.curDateMonth);
			// Controller.showScreen(new
			// CustomTransperentDialog("D.O.B. cannot be a future date"));
			// } else {
			// String validDateForlbl = new
			// SimpleDateFormat("dd-MMM-yyyy").format(new Date(date.getTime()));
			// ll.setText(validDateForlbl);
			// }
			// }
			// }
			// });
			// }
			// }
			// });
			// {
			// protected boolean navigationClick(int status, int time) {
			// showDatePicker();
			// return true;
			// }
			// };
			// hfm.add(ll[i]);
			// hfm.add(bf[i]);
			grid1.add(ll);

			// FieldChangeListener listener = new FieldChangeListener() {
			// public void fieldChanged(Field field, int context) {
			// if (field instanceof ButtonField) {
			// if (field == bf) {
			// showDatePicker();
			// }
			// }
			// }
			// };
			// bf.setChangeListener(listener);

			String[] r = { "Select", "Child", "Spouse", "Parent", "Other" };
			relation = new ObjectChoiceField("", r, (int) (Field.FOCUSABLE));
			grid1.add(relation);
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}

	// public static void showDatePicker() {
	// UiApplication.getUiApplication().invokeLater(new Runnable() {
	// public void run() {
	// final DateTimePicker dateTimePicker =
	// DateTimePicker.createInstance(Calendar.getInstance(),
	// "yyyy:MM:dd", null);
	// dateTimePicker.doModal();
	//
	// cal = dateTimePicker.getDateTime();
	// date = cal.getTime();
	//
	// if (date != null) {
	// selDate = new SimpleDateFormat("dd-MM-yyyy").format(new
	// Date(date.getTime()));
	// isFutureDate = doCheckFutureDate(selDate, Constants.curDate);
	// if (isFutureDate) {
	// globalLabel.setText(Constants.curDateMonth);
	// Controller.showScreen(new
	// CustomTransperentDialog("D.O.B. cannot be a future date"));
	// } else {
	// String validDateForlbl = new SimpleDateFormat("dd-MMM-yyyy").format(new
	// Date(date.getTime()));
	// globalLabel.setText(validDateForlbl);
	// }
	// }
	// }
	// });
	// }

	public static boolean doCheckFutureDate(String selectedDate, String currentDate) {
		String[] splitSelectedDate = StringSplitter.INSTANCE.split(selectedDate, "-");
		String[] splitCurrentDate = StringSplitter.INSTANCE.split(currentDate, "-");

		int currentDay = Integer.parseInt(splitCurrentDate[0]);
		int currentMonth = Integer.parseInt(splitCurrentDate[1]);
		int currentYear = Integer.parseInt(splitCurrentDate[2]);

		int selectedDay = Integer.parseInt(splitSelectedDate[0]);
		int selectedMonth = Integer.parseInt(splitSelectedDate[1]);
		int selectedYear = Integer.parseInt(splitSelectedDate[2]);

		if (selectedYear > currentYear) {
			return true;
		} else if (selectedMonth > currentMonth && selectedYear >= currentYear) {
			return true;
		} else if (selectedDay > currentDay && selectedMonth >= currentMonth && selectedYear >= currentYear) {
			return true;
		} else {
			return false;
		}
	}

}
