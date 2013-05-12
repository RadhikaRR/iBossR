package com.bb.webService;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransport;

import com.bb.connection.ConnectionClass;
import com.bb.constants.Constants;
import com.bb.customClass.LogEventClass;
import com.bb.customClass.ShowDialog;

public class CallWebService {

	private HttpTransport httpTransport;
	private String urlAgency = "";
	private int counter = 0;

	private Timer timer;
	private TimerTask timerTask;

	private boolean finishedWork = false;

	public static CallWebService INSTANCE = new CallWebService();

	public CallWebService() {

	}

	private void startTimer() {
		finishedWork = false;
		if (counter < 1) {
			timer = new Timer();
			timerTask = new TimerTask() {
				public void run() {
					counter = 0;
					finishedWork = true;
					ShowDialog.INSTANCE.status("Oops! Could not load content, Please try after some time");
				}
			};
			timer.schedule(timerTask, 60 * 1000);
		}
	}

	public boolean authenticateWS(String imei) {
		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();
				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.AUTHENTICATIONURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.AUTHENTICATIONURL);
				}

				SoapObject request = new SoapObject(Constants.AUTHENTICATIONNAMESPACE, Constants.AUTHENTICATIONMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				// request.addProperty(Constants.METHOD_PARAM_IMEI,
				// "352060041284173");
				 request.addProperty(Constants.METHOD_PARAM_IMEI,
				 "356186040469253");
//				request.addProperty(Constants.METHOD_PARAM_IMEI, "35798902533787701");
//				 request.addProperty(Constants.METHOD_PARAM_IMEI, imei);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedAuthWS(object);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Exception in Authentication:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
//					ShowDialog.INSTANCE.dialog(ioException.getMessage());
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Authentication:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Authentication Failed! Please check connection");
				}
			}
		}
		return true;
	}

	public boolean getVehicleMakeWS(String imei) {

		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();

				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.METHOD_PARAM_IMEI, imei);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedGetVehicleMakeWS(object);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in Car Make:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Car Make:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Failed to retrive car make! Please try after some time");
				}
			}
		}
		return true;
	}

	public boolean getVehicleModelWS(String imei, String vehicleCode, String vehicleMake) {

		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();

				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.VEHICLE_MODEL_METHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.METHOD_PARAM_IMEI, imei);
				request.addProperty(Constants.VehicleTypeCode, vehicleCode);
				request.addProperty(Constants.pVehicleMakeCode, vehicleMake);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedGetVehicleModelWS(object);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in car model:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error! Please check connection");
				}

			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Car Make:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Failed to retrive car model! Please try after some time");
				}
			}
		}
		return true;
	}

	public boolean getSubVehicleWS(String imei, String vehicleCode, String makecode, String vehicleMake) {

		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();

				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.SubVehicleMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.subpImeiNo, imei);
				request.addProperty(Constants.subpVehicleTypeCode, vehicleCode);
				request.addProperty(Constants.subpVehicleMakeCode, makecode);
				request.addProperty(Constants.subpVehicleModel, vehicleMake);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedSubVehicleModelWS(object);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in car sub-model:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}

			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in car sub-model:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Failed to retrive car sub-model! Please try after some time");
				}
			}
		}
		return true;
	}

	public boolean VehicleSubTypeWS(String imei, String VehicleTypeCode, String VehicleMakeCode, String VehicleModel,
			String VehicleSubType, String RegistrationDate, String Pincode, String pBussType, String pUserName) {

		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();

				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.VehicleSubTypeMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.VehicleSubTypeImeiNo, imei);
				request.addProperty(Constants.pVehicleTypeCode, VehicleTypeCode);
				request.addProperty(Constants.VehicleSubTypeMakeCode, VehicleMakeCode);
				request.addProperty(Constants.pVehicleModel, VehicleModel);
				request.addProperty(Constants.pVehicleSubType, VehicleSubType);
				request.addProperty(Constants.pRegistrationDate, RegistrationDate);
				request.addProperty(Constants.pPincode, Pincode);
				request.addProperty(Constants.pBussType, pBussType);
				request.addProperty(Constants.pUserName, pUserName);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedVehicleSubTypeWS(object, pBussType, pUserName);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in calculating IDV:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}

			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in calculating IDV:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Failed to calculate IDV:! Please try after some time");
				}
			}
		}
		return true;
	}

	public boolean calculatePremiumWS(String pImeiNo, String pVecTypeCode, String pVecMakeCode, String pVecModel,
			String pVecSubType, String pPinCode, String pdate, String pVecNonElec, String pVecElec, String pVecCng,
			String pSumAssured, String pCalculatedIDV, String pcommercialdisc, String pdriveassure, String pncbValue,
			String pBussType, String pUserName) {

		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();

				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.getCalculateMotorPremiumMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.pImeiNo, pImeiNo);
				request.addProperty(Constants.pVecTypeCode, pVecTypeCode);
				request.addProperty(Constants.pvehiclemakecode, pVecMakeCode);
				request.addProperty(Constants.pvehiclemodel, pVecModel);
				request.addProperty(Constants.pvehiclesubtype, pVecSubType);
				request.addProperty(Constants.ppincode, pPinCode);
				request.addProperty(Constants.RegistrationDate, pdate);
				request.addProperty(Constants.pvehicleNonelec, pVecNonElec);
				request.addProperty(Constants.pvehicleElec, pVecElec);
				request.addProperty(Constants.pcngValue, pVecCng);
				request.addProperty(Constants.ppaSuminsured, pSumAssured);
				request.addProperty(Constants.pcalculatedidv, pCalculatedIDV);
				request.addProperty(Constants.pcommercialdisc, pcommercialdisc);
				request.addProperty(Constants.pdriveassure, pdriveassure);
				request.addProperty(Constants.pncbValue, pncbValue);
				request.addProperty(Constants.ppBussType, pBussType);
				request.addProperty(Constants.ppUserName, pUserName);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedCalculatePremiumWS(object, pCalculatedIDV,pImeiNo,  pVecTypeCode,  pVecMakeCode,  pVecModel,
							 pVecSubType,  pPinCode,  pdate,  pVecNonElec,  pVecElec,  pVecCng,
							 pSumAssured,  pCalculatedIDV,  pcommercialdisc,  pdriveassure,  pncbValue,
							 pBussType,  pUserName);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in Premium calculation:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}

			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Premium calculation:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Failed to calculate Premium calculation:! Please try after some time");
				}
			}
		}
		return true;
	}

	public boolean generateQuotationWS(String pImeiNo, String pCustomerName, String pUpdatedBy, String pParamList) {

		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();
				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}
				SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.getQuotationMETHOD);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				request.addProperty(Constants.qImeiNo, pImeiNo);
				request.addProperty(Constants.qCustomerName, pCustomerName);
				request.addProperty(Constants.qUpdatedBy, pUpdatedBy);
				request.addProperty(Constants.pParamList, pParamList);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedgenerateQuotationWS(object);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in Premium calculation:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}

			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Premium calculation:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Failed to calculate Premium calculation:! Please try after some time");
				}
			}
		}
		return true;
	}

	public boolean travelPlanWS(String imei, String UpdatedBy) {
		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();
				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.AUTHENTICATIONNAMESPACE, Constants.travelPlanMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.tImeiNo, imei);
				request.addProperty(Constants.tCustomerName, UpdatedBy);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedtravelPlanWS(object);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in Authentication:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Authentication:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Authentication Failed! Please check connection");
				}
			}
		}
		return true;
	}

	public boolean travelAreaWS(String imei, String UpdatedBy, String planName) {
		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();
				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.AUTHENTICATIONNAMESPACE, Constants.weoGetTravelAreaMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.aImeiNo, imei);
				request.addProperty(Constants.aUserName, UpdatedBy);
				request.addProperty(Constants.pTravelPlan, planName);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedttravelAreaWS(object);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in Authentication:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Authentication:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Authentication Failed! Please check connection");
				}
			}
		}
		return true;
	}

	public boolean travalPremiumWS(String imei, String pUpdatedBy, String stPlanName, String stArea,
			String tpTravelFromDate, String tpTravelToDate, String tpFamilyMember, String tpFamilyFlag,
			String pFamilyList, String tpLoading, String tpDiscount, String tpSpecialDiscount, String tpCommercialDisc,
			String tpCommDiscRate, String date, String pPassengerName, String familyY) {
		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();
				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.AUTHENTICATIONNAMESPACE, Constants.TravalPremiumMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.tpImeiNo, imei);
				request.addProperty(Constants.tpUserName, pUpdatedBy);
				request.addProperty(Constants.tpTravelPlan, stPlanName);
				request.addProperty(Constants.tpTravalArea, stArea);
				request.addProperty(Constants.tpTravelFromDate, tpTravelFromDate);
				request.addProperty(Constants.tpTravelToDate, tpTravelToDate);
				request.addProperty(Constants.tpFamilyMember, tpFamilyMember);
				request.addProperty(Constants.tpFamilyFlag, tpFamilyFlag);
				request.addProperty(Constants.pFamilyList, pFamilyList);
				request.addProperty(Constants.tpLoading, tpLoading);
				request.addProperty(Constants.tpDiscount, tpDiscount);
				request.addProperty(Constants.tpSpecialDiscount, tpSpecialDiscount);
				request.addProperty(Constants.tpCommercialDisc, tpCommercialDisc);
				request.addProperty(Constants.tpCommDiscRate, tpCommDiscRate);
				request.addProperty(Constants.tpDateOfBirth, date);
				request.addProperty(Constants.tpPassengerName, pPassengerName);

				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedtravalPremiumWS(object, familyY);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in Authentication:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Authentication:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Authentication Failed! Please check connection");
				}
			}
		}
		return true;
	}

	public boolean travalGenerateReferenceWS(String imei, String customerName, String pUpdatedBy, String pParamList,
			String familyY) {
		boolean nw = ConnectionClass.INSTANCE.checkNetworkConnection();
		if (nw == false) {
			ShowDialog.INSTANCE.status("Network Coverage is unavailable");
		} else {
			try {
				startTimer();
				if (Constants.connectTCP) {
					urlAgency = ConnectionClass.INSTANCE.connectTCP(Constants.iBossURL);
				} else if (Constants.connectBIS) {
					urlAgency = ConnectionClass.INSTANCE.connectBIS(Constants.iBossURL);
				}

				SoapObject request = new SoapObject(Constants.AUTHENTICATIONNAMESPACE,
						Constants.genTravalQuotParamaterMETHOD);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				request.addProperty(Constants.trImeiNo, imei);
				request.addProperty(Constants.trpCustomerName, customerName);
				request.addProperty(Constants.trpUpdatedBy, pUpdatedBy);
				request.addProperty(Constants.trpParamList, pParamList);
				request.addProperty(Constants.trpFamilyList, familyY);
			
				// envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				httpTransport = new HttpTransport(urlAgency);
				httpTransport.debug = true;
				httpTransport.call(Constants.SOAPACTION, envelope);

				SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
				Object object = (Object) resultsRequestSOAP;
				System.out.println("WS O/p is --------------------" + object.toString());

				timerTask.cancel();
				timer.cancel();
				if (finishedWork == false) {
					WebServiceManager.INSTANCE.returnedtravalGenerateReferenceWS(object);
				}

			} catch (final IOException ioException) {
				LogEventClass.logErrorEvent("Error in Authentication:" + ioException.getMessage());
				Constants.connectTCP = true;
				Constants.connectBIS = false;
				counter = 0;
				if (finishedWork == false) {
					timer.cancel();
					ShowDialog.INSTANCE.dialog("Connection Error!,Please check connection");
				}
			} catch (Exception e) {
				LogEventClass.logErrorEvent("Error in Authentication:" + e.getMessage());
				counter = counter + 1;
				if (counter <= 2) {
					while (counter == 2) {
						Constants.connectBIS = true;
						Constants.connectTCP = false;
						break;
					}
					return false;
				} else {
					counter = 0;
					Constants.connectBIS = false;
					Constants.connectTCP = true;
					ShowDialog.INSTANCE.dialog("Authentication Failed! Please check connection");
				}
			}
		}
		return true;
	}
}
