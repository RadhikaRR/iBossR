package com.bb.customClass;

import java.util.Calendar;
import java.util.Date;

import net.rim.device.api.i18n.Format;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.picker.DateTimePicker;

import com.bb.UiScreens.CalculateIDVScreen;
import com.bb.UiScreens.travelScreen;
import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.customFields.StringSplitter;

public class SelectDate {

	private static Date date;
	private static Calendar cal;
	public static String selDate;
	private static boolean isFutureDate;

	public static void showDatePicker(final int travel) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {

				final DateTimePicker dateTimePicker = DateTimePicker.createInstance(Calendar.getInstance(),
						"yyyy:MM:dd", null);
				dateTimePicker.doModal();

				cal = dateTimePicker.getDateTime();
				date = cal.getTime();

				if (date != null) {
					selDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date(date.getTime()));

					if (travel == 1) {
						isFutureDate = doCheckPastDate(selDate, Constants.curDate);
						if (isFutureDate) {
							travelScreen.showTodaysDate.setText(Constants.curDateMonth);
							Controller.showScreen(new CustomTransperentDialog("Date must be a Future date"));
						} else {
							String validDateForlbl = new SimpleDateFormat("dd-MMM-yyyy")
									.format(new Date(date.getTime()));
//							if (Constants.travelDate != null) {
//								Constants.travelDate = null;								
//							}
							Constants.travelDate = date;
							travelScreen.showTodaysDate.setText(validDateForlbl);

							if (!(Constants.journyDays.equals("0"))) {
								Date d3 = new Date();
								d3 = Constants.travelDate;
								d3.setTime(Constants.travelDate.getTime()
										+ (long) (Integer.parseInt(Constants.journyDays)) * 24 * 60 * 60 * 1000);
								Format dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
								String newDateString = dateFormat.format(d3);

								travelScreen.showReturnDate.setText(newDateString);
								travelScreen.journeyDaysLabel2.setText(Constants.journyDays);

								// UiApplication.getUiApplication().invokeLater(new
								// Runnable() {
								// public void run() {
								// travelScreen.showReturnDate.setText(newDateString);//
								// = new LabelField(newDateString);
								// travelScreen.journeyDaysLabel2.setText(Constants.journyDays);//
								// = new LabelField(Constants.journyDays);
								// }
								// });
							}
//							else{
//								if (Constants.travelDate != null) {
//									Constants.travelDate = null;								
//								}
//								//Constants.travelDate = new Date(System.currentTimeMillis());
//							}
						}

					} else if (travel == 2) {

						isFutureDate = doCheckFutureDate(selDate, Constants.curDate);
						if (isFutureDate) {
							travelScreen.dobEdit.setText(Constants.curDateMonth);
							Controller.showScreen(new CustomTransperentDialog("D.O.B. cannot be a Future date"));
						} else {
							String validDateForlbl = new SimpleDateFormat("dd-MMM-yyyy")
									.format(new Date(date.getTime()));
							travelScreen.dobEdit.setText(validDateForlbl);
						}

					} else {
						isFutureDate = doCheckFutureDate(selDate, Constants.curDate);
						if (isFutureDate) {
							CalculateIDVScreen.showTodaysDate.setText(Constants.curDateMonth);
							Controller.showScreen(new CustomTransperentDialog("Future date is not allowed."));
						} else {
							String validDateForlbl = new SimpleDateFormat("dd-MMM-yyyy")
									.format(new Date(date.getTime()));
							CalculateIDVScreen.showTodaysDate.setText(validDateForlbl);
						}
					}
				}
			}
		});
	}

	public static void showReturnDatePicker() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {

				final DateTimePicker dateTimePicker = DateTimePicker.createInstance(Calendar.getInstance(),
						"yyyy:MM:dd", null);
				dateTimePicker.doModal();

				cal = dateTimePicker.getDateTime();
				Date date1 = cal.getTime();

				if (date1 != null) {
					selDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date(date1.getTime()));

					isFutureDate = doCheckPastDate(selDate, Constants.curDate);
					if (isFutureDate) {
						travelScreen.showReturnDate.setText(Constants.curDateMonth);
						Controller.showScreen(new CustomTransperentDialog("Date must be a Future date"));
					} else {
						String validDateForlbl = new SimpleDateFormat("dd-MMM-yyyy").format(new Date(date1.getTime()));
//						if(Constants.returnDate !=null){
//							Constants.returnDate = null;
//							Constants.returnDate = date1;
//						}
						Constants.returnDate = date1;
						travelScreen.showReturnDate.setText(validDateForlbl);

						long days = travelScreen.daysBetween(Constants.returnDate, Constants.travelDate);
						travelScreen.journeyDaysLabel2.setText("" + days);
					}
				}
			}
		});
	}

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

	public static boolean doCheckPastDate(String selectedDate, String currentDate) {
		String[] splitSelectedDate = StringSplitter.INSTANCE.split(selectedDate, "-");
		String[] splitCurrentDate = StringSplitter.INSTANCE.split(currentDate, "-");

		int currentDay = Integer.parseInt(splitCurrentDate[0]);
		int currentMonth = Integer.parseInt(splitCurrentDate[1]);
		int currentYear = Integer.parseInt(splitCurrentDate[2]);

		int selectedDay = Integer.parseInt(splitSelectedDate[0]);
		int selectedMonth = Integer.parseInt(splitSelectedDate[1]);
		int selectedYear = Integer.parseInt(splitSelectedDate[2]);

		if (selectedYear < currentYear) {
			return true;
		} else if (selectedMonth < currentMonth && selectedYear <= currentYear) {
			return true;
		} else if (selectedDay < currentDay && selectedMonth <= currentMonth && selectedYear <= currentYear) {
			return true;
		} else {
			return false;
		}
	}
}
