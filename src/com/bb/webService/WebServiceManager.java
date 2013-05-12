package com.bb.webService;

import java.util.Date;
import java.util.Hashtable;

import net.rim.device.api.i18n.Format;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Status;

import com.bb.UiScreens.CalculateIDVScreen;
import com.bb.UiScreens.CalculatePremiumScreen;
import com.bb.UiScreens.FamilyMemberScreen;
import com.bb.UiScreens.FirstScreen;
import com.bb.UiScreens.PremiumDetailsTableScreen;
import com.bb.UiScreens.StartupScreen;
import com.bb.UiScreens.travelCalculatedPremiumScreen;
import com.bb.UiScreens.travelScreen;
import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.controller.MainScreenClass;
import com.bb.customClass.AppUpdater;
import com.bb.customClass.CustomTransperentDialog;
import com.bb.customClass.PopupSpinnerScreen;
import com.bb.customClass.ShowDialog;
import com.bb.customFields.StringSplitter;
import com.bb.customFields.StringTokenizer;

public class WebServiceManager {

	public static WebServiceManager INSTANCE = new WebServiceManager();
	public static String[] NAVRows = new String[20];
	public static String[] FundAccDetailsRows = new String[20];

	String areaResponse;
	String familyFlag;
	String maxDate;
	String minDate;

	public static String[] PolicyFundString = new String[50];

	public WebServiceManager() {
	}

	public Hashtable converObjectToHashTable(String string) {
		Hashtable hashtable = new Hashtable();
		try {
			string = string.substring(string.indexOf("{") + 1, string.length() - 1);
			string = string.substring(string.indexOf("{") + 1);
			string = string.replace('}', ' ');
			StringTokenizer tokenizer = new StringTokenizer(string, ";");
			while (tokenizer.hasMoreElements()) {
				String token = (String) tokenizer.nextElement();
				if (token.indexOf("=") != -1) {
					String key = token.substring(0, token.indexOf("="));
					String value = (token.substring(token.indexOf("=") + 1)).trim();
					if (!(value.equals("null") || value.equals(""))) {
						hashtable.put(key.trim(), value.trim());
					}
				}
			}
		} catch (Exception e) {
			ShowDialog.INSTANCE.status("Fail to parsed object in convertObjectToHashTable" + e.toString());
		}
		return hashtable;
	}

	public void returnedAuthWS(Object object) {
		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {
				String authResponse = (String) hashtable.get("stringval10");
				if (authResponse.equals("USER-IS-ENABLED")) {
					handleAuthResponse(hashtable);
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen);
							}
							Status.show("User is not enabled! Exitting");
							System.exit(0);
						}
					});
				}
			}
		} else {
			ShowDialog.INSTANCE.dialog("Unable to retrive login Details, Please try after some time");
		}
	}

	public void handleAuthResponse(Hashtable hashtable) {

		Constants.hashtavleFirstScreen = hashtable;

		String modules = (String) hashtable.get("stringval2");
		// String modules = "1~2~3~4~5~6~7~8~9~10~11";
		Constants.moduleVisible = StringSplitter.INSTANCE.split(modules, "~");

		String newVersion = (String) hashtable.get("stringval38");
		boolean downloadFlag = false;
		if (newVersion != null && !newVersion.equalsIgnoreCase("null")) {
			downloadFlag = AppUpdater.INSTANCE.doCheckForUpdates(newVersion);
		}

		if (downloadFlag) {
			String downloadPath = (String) hashtable.get("stringval39");
			if (downloadPath != null && !downloadPath.equalsIgnoreCase("null")) {
				String fullDownloadpath = downloadPath + Constants.newAppDownloadAppendURL;

				boolean flag = true;
				flag = AppUpdater.INSTANCE.openDownloadURL(fullDownloadpath);
				while (flag == false) {
					flag = AppUpdater.INSTANCE.openDownloadURL(fullDownloadpath);
				}
			}
		} else {
			Controller.showScreen(new FirstScreen(hashtable, Constants.moduleVisible));
		}
	}

	public void returnedGetVehicleMakeWS(Object object) {

		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {
				String authResponse = (String) hashtable.get("stringval10");
				if (authResponse.equals("SUCCESS")) {
					handleResponse(hashtable);
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen);
							}
						}
					});
					Controller.showScreen(new CustomTransperentDialog("Error loading VehicleMake"));
				}
			}
		} else {
			ShowDialog.INSTANCE.dialog("Unable to retrive VehicleMake Details, Please try after some time");
		}
	}

	public void returnedGetVehicleModelWS(Object object) {

		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {

				String authResponse = (String) hashtable.get("stringval10");
				if (authResponse.equals("SUCCESS")) {
					handleModelResponse(hashtable);
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen);
							}
						}
					});
					Controller.showScreen(new CustomTransperentDialog("Error loading VehicleModel"));
				}
			}
		} else {
			ShowDialog.INSTANCE.dialog("Unable to retrive VehicleModel Details, Please try after some time");
		}
	}

	public void handleResponse(Hashtable hashtable) {
		String[] string = new String[100];

		String stringval = " ";
		Hashtable hash = new Hashtable();
		for (int j = 0; j < 8; j++) {
			String st = (String) hashtable.get("stringval" + (j + 1));
			if (st == null || st.equalsIgnoreCase("x")) {
			} else {
				hash.put("stringval" + (j + 1), st);
				stringval = stringval + (String) hash.get("stringval" + (j + 1));
			}
			System.out.println("");
		}
		String str = "~Select#Select" + stringval;
		string = StringSplitter.INSTANCE.split(str, "~");

		String[] string2 = new String[2];
		Constants.carMake = new String[string.length];
		Constants.carModel = new String[string.length];

		System.out.println("");
		for (int j = 0; j < string.length; j++) {
			string2 = StringSplitter.INSTANCE.split(string[j], "#");
			Constants.carMake[j] = string2[0];
			Constants.carModel[j] = string2[1];
		}

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
				Controller.showScreen(new CalculateIDVScreen());
			}
		});
	}

	public void handleModelResponse(Hashtable hashtable) {
		String[] string = new String[100];

		String stringval = " ";
		Hashtable hash = new Hashtable();
		for (int j = 0; j < 8; j++) {
			String st = (String) hashtable.get("stringval" + (j + 1));
			if (st == null || st.equalsIgnoreCase("x")) {
			} else {
				hash.put("stringval" + (j + 1), st);
				stringval = stringval + (String) hash.get("stringval" + (j + 1));
			}
			System.out.println("");
		}
		String str = "~Select#Select" + stringval;
		string = StringSplitter.INSTANCE.split(str, "~");

		String[] string2 = new String[2];
		Constants.typeCode = new String[string.length];
		Constants.makeCode = new String[string.length];

		System.out.println("");
		for (int j = 0; j < string.length; j++) {
			string2 = StringSplitter.INSTANCE.split(string[j], "#");
			Constants.typeCode[j] = string2[0];
			Constants.makeCode[j] = string2[1];
		}
		System.out.println("'");

		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				Screen screen = UiApplication.getUiApplication().getActiveScreen();
				if (screen instanceof PopupSpinnerScreen) {
					UiApplication.getUiApplication().popScreen(screen);
				}
				CalculateIDVScreen.carmodel.setChoices(Constants.makeCode);
			}
		});
		// CalculateIDVScreen.carmodel.setChoices(Constants.makeCode);
	}

	public void returnedSubVehicleModelWS(Object object) {

		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {

				String authResponse = (String) hashtable.get("stringval10");
				if (authResponse.equals("SUCCESS")) {
					handleSubResponse(hashtable);
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen);
							}
						}
					});
					Controller.showScreen(new CustomTransperentDialog("Error loading sub model"));
				}
			}
		} else {
			ShowDialog.INSTANCE.dialog("Unable to retrive Vehicle sub model Details, Please try after some time");
		}
	}

	public void handleSubResponse(Hashtable hashtable) {
		String[] string = new String[100];

		String stringval = " ";
		Hashtable hash = new Hashtable();
		for (int j = 0; j < 8; j++) {
			String st = (String) hashtable.get("stringval" + (j + 1));
			if (st == null || st.equalsIgnoreCase("x")) {
			} else {
				hash.put("stringval" + (j + 1), st);
				stringval = stringval + (String) hash.get("stringval" + (j + 1));
			}
			System.out.println("");
		}
		String str = "~Select#Select" + stringval;
		string = StringSplitter.INSTANCE.split(str, "~");

		String[] string2 = new String[2];
		Constants.VehicleSubType = new String[string.length];
		Constants.VehicleSubName = new String[string.length];

		System.out.println("");
		for (int j = 0; j < string.length; j++) {
			string2 = StringSplitter.INSTANCE.split(string[j], "#");
			Constants.VehicleSubType[j] = string2[0];
			Constants.VehicleSubName[j] = string2[1];
		}
		System.out.println("'");
		// Controller.showScreen(new CalculateIDVScreen());
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				Screen screen = UiApplication.getUiApplication().getActiveScreen();
				if (screen instanceof PopupSpinnerScreen) {
					UiApplication.getUiApplication().popScreen(screen);
				}
				CalculateIDVScreen.cartype.setChoices(Constants.VehicleSubName);
			}
		});
		// CalculateIDVScreen.carmodel.setChoices(Constants.makeCode)
	}

	public void returnedVehicleSubTypeWS(Object object, String pBussType, String pUserName) {

		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {

				String authResponse = (String) hashtable.get("stringval20");
				if (authResponse.equals("SUCCESS")) {
					handleVehicleSubTypeResponse(hashtable, pBussType, pUserName);
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {

						public void run() {

							if (DeviceInfo.isSimulator()) {
								Screen screen1 = UiApplication.getUiApplication().getActiveScreen();
								UiApplication.getUiApplication().popScreen(screen1);

								Screen screen = UiApplication.getUiApplication().getActiveScreen();
								if (screen instanceof PopupSpinnerScreen) {
									UiApplication.getUiApplication().popScreen(screen);
								}
								Controller.showScreen(new CustomTransperentDialog("Error loading IDV"));

							} else {
								Screen screen = UiApplication.getUiApplication().getActiveScreen();
								if (screen instanceof PopupSpinnerScreen) {
									UiApplication.getUiApplication().popScreen(screen);
								}
								Controller.showScreen(new CustomTransperentDialog("Error loading IDV"));
							}
						}
					});
				}
			}
		} else {
			ShowDialog.INSTANCE.dialog("Unable to retrive IDV Details, Please try after some time");
		}
	}

	public void handleVehicleSubTypeResponse(Hashtable hashtable, final String pBussType, final String pUserName) {
		final String calculateIdv = (String) hashtable.get("stringval10");
		final String stringval11 = (String) hashtable.get("stringval11");
		final String stringval12 = (String) hashtable.get("stringval12");
		final String stringval13 = (String) hashtable.get("stringval13");
		final String stringval14 = (String) hashtable.get("stringval14");

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
				Controller.showScreen(new CalculatePremiumScreen(calculateIdv, pBussType, pUserName, stringval11,
						stringval12, stringval13, stringval14));
			}
		});

	}

	public void returnedCalculatePremiumWS(Object object, String pCalculatedIDV, String pImeiNo, String pVecTypeCode,
			String pVecMakeCode, String pVecModel, String pVecSubType, String pPinCode, String pdate,
			String pVecNonElec, String pVecElec, String pVecCng, String pSumAssured, String pCalculatedIDVv,
			String pcommercialdisc, String pdriveassure, String pncbValue, String pBussType, String pUserName) {
		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {

				String authResponse = (String) hashtable.get("stringval20");
				if (authResponse.equals("SUCCESS")) {
					handleCalculatePremiumResponse(hashtable, pCalculatedIDV, pImeiNo, pVecTypeCode, pVecMakeCode,
							pVecModel, pVecSubType, pPinCode, pdate, pVecNonElec, pVecElec, pVecCng, pSumAssured,
							pCalculatedIDVv, pcommercialdisc, pdriveassure, pncbValue, pBussType, pUserName);
				} else {

					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							UiApplication ui = UiApplication.getUiApplication();
							int screenCount = ui.getScreenCount();
							for (int i = 0; i < screenCount; i++) {
								Screen activeScreen = ui.getActiveScreen();
								if (activeScreen instanceof CalculatePremiumScreen) {
									break;
								} else {
									ui.popScreen(activeScreen);
								}
							}
							Controller.showScreen(new CustomTransperentDialog("Error loading premium"));
						}
					});
				}
			}
		} else {
			ShowDialog.INSTANCE.dialog("Unable to retrive premium Details, Please try after some time");
		}
	}

	public void handleCalculatePremiumResponse(final Hashtable hashtable, final String pCalculatedIDV,
			final String pImeiNo, final String pVecTypeCode, final String pVecMakeCode, final String pVecModel,
			final String pVecSubType, final String pPinCode, final String pdate, final String pVecNonElec,
			final String pVecElec, final String pVecCng, final String pSumAssured, final String pCalculatedIDVv,
			final String pcommercialdisc, final String pdriveassure, final String pncbValue, final String pBussType,
			final String pUserName) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication ui = UiApplication.getUiApplication();
				int screenCount = ui.getScreenCount();
				for (int i = 0; i < screenCount; i++) {
					Screen activeScreen = ui.getActiveScreen();
					if (activeScreen instanceof CalculatePremiumScreen) {
						break;
					} else {
						ui.popScreen(activeScreen);
					}
				}
				Controller.showScreen(new PremiumDetailsTableScreen(hashtable, pCalculatedIDV, pImeiNo, pVecTypeCode,
						pVecMakeCode, pVecModel, pVecSubType, pPinCode, pdate, pVecNonElec, pVecElec, pVecCng,
						pSumAssured, pCalculatedIDVv, pcommercialdisc, pdriveassure, pncbValue, pBussType, pUserName));
			}
		});
	}

	public void returnedgenerateQuotationWS(Object object) {
		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {
				final String authResponse = (String) hashtable.get("pquotenoOut");

				// UiApplication.getUiApplication().invokeLater(new Runnable() {
				// public void run() {
				// Screen screen =
				// UiApplication.getUiApplication().getActiveScreen();
				// if (screen instanceof PopupSpinnerScreen) {
				// UiApplication.getUiApplication().popScreen(screen);
				// }
				// //Controller.showScreen(new
				// CustomTransperentDialog("Generated Reference no. is\n" +
				// authResponse));
				// Dialog.alert("Generated Reference no. is\n" + authResponse)
				// }
				// });

				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert("Reference No -\n" + authResponse);
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
			}
		}
	}

	public void returnedtravelPlanWS(Object object) {
		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {
				String authResponse = (String) hashtable.get("stringval10");
				if (authResponse.equals("SUCCESS")) {
					handledReturnedtravelPlanWS(hashtable);
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen);
							}
							Controller.showScreen(new CustomTransperentDialog("error in loading plan name"));
						}
					});
				}
			}
		}
	}

	public void handledReturnedtravelPlanWS(Hashtable hashtable) {

		String commDiscRate = (String) hashtable.get("stringval4");

		Constants.commDiscRate = commDiscRate.substring(10);

		String[] string = new String[100];
		String stringval = " ";
		Hashtable hash = new Hashtable();
		for (int j = 0; j < 2; j++) {
			String st = (String) hashtable.get("stringval" + (j + 1));
			if (st == null || st.equalsIgnoreCase("x")) {
			} else {
				hash.put("stringval" + (j + 1), st);
				stringval = stringval + (String) hash.get("stringval" + (j + 1));
			}
			System.out.println("");
		}
		String str = "~Select" + stringval.substring(2);
		string = StringSplitter.INSTANCE.split(str, "~");

		Constants.planName = new String[string.length];

		System.out.println("");
		for (int j = 0; j < string.length; j++) {
			Constants.planName[j] = string[j];
		}
		System.out.println("");
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				Screen screen = UiApplication.getUiApplication().getActiveScreen();
				if (screen instanceof PopupSpinnerScreen) {
					UiApplication.getUiApplication().popScreen(screen);
				}
				Controller.showScreen(new travelScreen());
			}
		});
	}

	public void returnedttravelAreaWS(Object object) {
		if (object != null) {
			String response = object.toString();
			Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {
				String authResponse = (String) hashtable.get("stringval10");
				if (authResponse.equals("SUCCESS")) {
					handleReturnedttravelAreaWS(hashtable);
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen);
							}
							Controller.showScreen(new CustomTransperentDialog("error in loading plan name"));
						}
					});
				}
			}
		}
	}

	public void handleReturnedttravelAreaWS(Hashtable hashtable) {

		Constants.journyDays = (String) hashtable.get("stringval3");
		areaResponse = (String) hashtable.get("stringval1");
		familyFlag = (String) hashtable.get("stringval4");
		maxDate = (String) hashtable.get("stringval6");
		minDate = (String) hashtable.get("stringval5");

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {

				// Constants.journyDays = "365";

				if (!(Constants.journyDays.equals("0"))) {
					Date d3 = new Date();
					d3 = Constants.travelDate;
					d3.setTime((Constants.travelDate.getTime()) + (long) (Integer.parseInt(Constants.journyDays)) * 24
							* 60 * 60 * 1000);
					Format dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					final String newDateString = dateFormat.format(d3);

					travelScreen.showReturnDate.setText(newDateString);
					travelScreen.journeyDaysLabel2.setText(Constants.journyDays);
					if (travelScreen.btnToDt.isVisible() == true) {
						travelScreen.vfmToDate.delete(travelScreen.btnToDt);
					}
				} else {
					travelScreen.showTodaysDate.setText(Constants.curDateMonth);
					travelScreen.showReturnDate.setText(Constants.curDateMonth);
					travelScreen.journeyDaysLabel2.setText(Constants.journyDays);

					if (travelScreen.btnToDt.isVisible() == false) {
						travelScreen.vfmToDate.add(travelScreen.btnToDt);
					}
					System.out.println("");
				}

				String[] minD = StringSplitter.INSTANCE.split(minDate, "#M#");
				Constants.minName = new String[minD.length];
				Constants.minValue = new String[minD.length];
				for (int min = 0; min < minD.length; min++) {
					String[] strMin = StringSplitter.INSTANCE.split(minD[min], "~");
					Constants.minName[min] = strMin[0];
					Constants.minValue[min] = strMin[1];
				}
				System.out.println("");

				String[] maxD = StringSplitter.INSTANCE.split(maxDate, "#M#");
				Constants.maxName = new String[maxD.length];
				Constants.maxValue = new String[maxD.length];
				for (int max = 0; max < maxD.length; max++) {
					String[] strMax = StringSplitter.INSTANCE.split(maxD[max], "~");
					Constants.maxName[max] = strMax[0];
					Constants.maxValue[max] = strMax[1];
				}

				System.out.println("");

				Constants.familyFlag = familyFlag.substring(12);
				System.out.println("");

				System.out.println("");
				if (Constants.familyFlag.equals("Y")) {

					if (travelScreen.btnPremium.isVisible() == true) {
						// travelScreen.vfmToDate.delete(travelScreen.btnToDt);
						travelScreen.vfm.delete(travelScreen.btnPremium);
					}
					if (travelScreen.hfmfamilyMember.isVisible() == false
							&& travelScreen.btnTravelPremium.isVisible() == false) {
						travelScreen.vfm.add(travelScreen.hfmfamilyMember);
						travelScreen.vfm.add(travelScreen.btnTravelPremium);
					}

				} else {

					if (travelScreen.hfmfamilyMember.isVisible() == true
							&& travelScreen.btnTravelPremium.isVisible() == true) {
						travelScreen.vfm.delete(travelScreen.hfmfamilyMember);
						travelScreen.vfm.delete(travelScreen.btnTravelPremium);
					}

					if (travelScreen.btnPremium.isVisible() == false) {
						// travelScreen.vfmToDate.add(travelScreen.btnToDt);
						travelScreen.vfm.add(travelScreen.btnPremium);
					}
				}

				String[] string = new String[100];
				String str = "~Select" + areaResponse;

				string = StringSplitter.INSTANCE.split(str, "~");

				Constants.areaName = new String[string.length];

				System.out.println("");
				for (int j = 0; j < string.length; j++) {
					Constants.areaName[j] = string[j];
				}
				System.out.println("");

				Screen screen = UiApplication.getUiApplication().getActiveScreen();
				if (screen instanceof PopupSpinnerScreen) {
					UiApplication.getUiApplication().popScreen(screen);
				}
				travelScreen.Area.setChoices(Constants.areaName);

			}
		});

	}

	public void returnedtravalPremiumWS(Object object, String familyY) {
		if (object != null) {
			String response = object.toString();
			final Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {
				String authResponse = (String) hashtable.get("stringval10");
				if (authResponse.equals("SUCCESS")) {
					handleReturnedtravalPremiumWS(hashtable, familyY);
				} else {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Screen screen = UiApplication.getUiApplication().getActiveScreen();
							if (screen instanceof PopupSpinnerScreen) {
								UiApplication.getUiApplication().popScreen(screen);
							}
							Controller.showScreen(new CustomTransperentDialog((String) hashtable.get("stringval9")));
						}
					});
				}
			}
		}
	}

	public void handleReturnedtravalPremiumWS(Hashtable hashtable, final String familyY) {

		final String pParamList = (String) hashtable.get("stringval8");

		final String[] premiumName = new String[4];
		final String[] premiumValue = new String[4];
		System.out.println("");
		for (int i = 0; i < 4; i++) {
			String NV = (String) hashtable.get("stringval" + (i + 1));

			String[] nameVal = StringSplitter.INSTANCE.split(NV, "~");

			premiumName[i] = nameVal[0];
			premiumValue[i] = nameVal[1];
		}
		// System.out.println("")
		// UiApplication.getUiApplication().invokeLater(new Runnable() {
		// public void run() {
		// Screen screen = UiApplication.getUiApplication().getActiveScreen();
		// if (screen instanceof PopupSpinnerScreen) {
		// UiApplication.getUiApplication().popScreen(screen);
		// }
		// Controller.showScreen(new travelCalculatedPremiumScreen(premiumName,
		// premiumValue));
		// }
		// });

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication ui = UiApplication.getUiApplication();
				int screenCount = ui.getScreenCount();
				for (int i = 0; i < screenCount; i++) {
					Screen activeScreen = ui.getActiveScreen();
					if (activeScreen instanceof FamilyMemberScreen) {
						break;
					} else {
						ui.popScreen(activeScreen);
					}
				}
				Controller
						.showScreen(new travelCalculatedPremiumScreen(premiumName, premiumValue, familyY, pParamList));
			}
		});
	}

	public void returnedtravalGenerateReferenceWS(Object object) {
		if (object != null) {
			String response = object.toString();
			final Hashtable hashtable = converObjectToHashTable(response);
			if (hashtable != null) {
				final String refNo = (String) hashtable.get("pquotenoOut");

				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {

						Dialog.alert("Reference No. -\n" + refNo);

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
						Controller.showScreen(new FirstScreen(Constants.hashtavleFirstScreen, Constants.moduleVisible));
					}
				});
			}
		}
	}
}
