
package com.bb.constants;

import java.util.Date;
import java.util.Hashtable;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Ui;

import com.bb.customClass.ImeiClass;

public class Constants {
	
	public static final long GUID = 0x38296bd474aa6978L;
	
	public static final String APP_NAME = "iBoss";
	
	public static final String SMSFLAG = "smsflag";
	
	public static final String newAppDownloadAppendURL = "/iBoss/BlackBerry/iBoss.jad";
	public static String curDate = new SimpleDateFormat("dd-MM-yyyy").formatLocal(System.currentTimeMillis());
	public static String curDateMonth = new SimpleDateFormat("dd-MMM-yyyy").formatLocal(System.currentTimeMillis());	
	
	public static Font font = Font.getDefault().derive(Font.PLAIN, 6, Ui.UNITS_pt);
	public static Font fontVerySmall = Font.getDefault().derive(Font.PLAIN, 5, Ui.UNITS_pt);
	public static Font fontBold = Font.getDefault().derive(Font.BOLD, 7, Ui.UNITS_pt);
	public static Font fontBold9 = Font.getDefault().derive(Font.BOLD, 9, Ui.UNITS_pt);
	public static Font fontBold6 = Font.getDefault().derive(Font.BOLD, 6, Ui.UNITS_pt);
	public static Font fontBold5 = Font.getDefault().derive(Font.BOLD, 5, Ui.UNITS_pt);
	public static Font fontUnderline = Font.getDefault().derive(Font.UNDERLINED | Font.BOLD, 7, Ui.UNITS_pt);
	
	public static boolean connectTCP = true;
	public static boolean connectBIS = false;
	
	public static final String IMEI = ImeiClass.INSTANCE.getIMEINumber();
	
	public static Hashtable hashtavleFirstScreen;
	
	public static String[] moduleVisible;
	public static boolean iMotorModuleVisible = false;
	public static boolean iTravelModuleVisible = false;
	
	public static String[] carMake ;
	public static String[] carModel ;
	
	public static String[] typeCode;
	public static String[] makeCode;
	
	public static String[] VehicleSubType;
	public static String[] VehicleSubName;
	
	public static String[] planName ;
	
	public static String[] areaName ;
	
	public static String journyDays = "0";
	public static String familyFlag = null;	
	
	public static String commDiscRate;
	
	public static Date travelDate;
	public static Date returnDate;
	
	public static String[] minName;
	public static String[] minValue;
	
	public static String[] maxName;
	public static String[] maxValue;	
	
	public static final String AUTHENTICATIONURL = "http://web7.bajajallianz.com/BagicWap/BagicWap";
	public static final String AUTHENTICATIONNAMESPACE = "http://com.bajajallianz/BagicWap.xsd";
	public static final String AUTHENTICATIONSOAPACTION = "BagicWapPortType";
	public static final String AUTHENTICATIONMETHOD = "verifyUserDtls";	
	
	public static String iBossURL = "http://web7.bajajallianz.com/BagicGenWap/BagicGenWap";
	public static String NAMESPACE = "http://com.bajajallianz/BagicGenWap.xsd";
	public static String SOAPACTION = "BagicGenWapPortType";	
	public static String METHOD= "getVehicleMake";
	
	public static String  METHOD_PARAM_IMEI= "pImeiNo";
	public static String  VehicleTypeCode= "pVehicleTypeCode";
	public static String  pVehicleMakeCode= "pVehicleMakeCode";
	public static String VEHICLE_MODEL_METHOD= "getVechModel";
	
	public static String subpImeiNo = "pImeiNo";
	public static String subpVehicleTypeCode="pVehicleTypeCode";
	public static String subpVehicleMakeCode="pVehicleMakeCode";
	public static String subpVehicleModel="pVehicleModel";
	public static String SubVehicleMETHOD= "getSubVechType";	
	
	public static String VehicleSubTypeMETHOD= "getIdvValue";
	public static String VehicleSubTypeImeiNo = "pImeiNo";
	public static String pVehicleTypeCode="pVehicleTypeCode";
	public static String VehicleSubTypeMakeCode="pVehicleMakeCode";
	public static String pVehicleModel="pVehicleModel";
	public static String pVehicleSubType="pVehicleSubType";
	public static String pRegistrationDate= "pRegistrationDate";
	public static String pPincode= "pPincode";	
	public static String pBussType= "pBussType";
	public static String pUserName= "pUserName";	
	
	public static String getCalculateMotorPremiumMETHOD= "getCalculateMotorPremium";
	public static String pImeiNo = "pImeiNo";
	public static String pVecTypeCode = "pVecTypeCode";
	public static String pvehiclemakecode="pvehiclemakecode";
	public static String pvehiclemodel="pvehiclemodel";
	public static String pvehiclesubtype="pvehiclesubtype";
	public static String ppincode="ppincode";
	public static String RegistrationDate= "pRegistrationDate";
	public static String pvehicleNonelec= "pvehicleNonelec";
	public static String pvehicleElec= "pvehicleElec";
	public static String pcngValue= "pcngValue";
	public static String ppaSuminsured= "ppaSuminsured";
	public static String pcalculatedidv= "pcalculatedidv";	
	public static String pcommercialdisc= "pcommercialdisc";
	public static String pdriveassure= "pdriveassure";
	public static String pncbValue= "pncbValue";
	public static String ppBussType= "pBussType";
	public static String ppUserName= "pUserName";
	
	public static String getQuotationMETHOD= "generateQuotationParamater";
	public static String qImeiNo = "pImeiNo";
	public static String qCustomerName = "pCustomerName";	
	public static String qUpdatedBy= "pUpdatedBy";
	public static String pParamList= "pParamList";	
	
	public static String travelPlanMETHOD= "getTravelPlan";
	public static String tImeiNo = "pImeiNo";
	public static String tCustomerName = "pUserName";
	
	public static String weoGetTravelAreaMETHOD= "weoGetTravelArea";
	public static String aImeiNo = "pImeiNo";
	public static String aUserName = "pUserName";
	public static String pTravelPlan = "pTravelPlan";
	
	public static String TravalPremiumMETHOD= "getTravalPremium";
	public static String tpImeiNo = "pImeiNo";
	public static String tpUserName = "pUserName";
	public static String tpTravelPlan = "pTravelPlan";	
	public static String tpTravalArea = "pTravalArea";
	public static String tpTravelFromDate = "pTravelFromDate";
	public static String tpTravelToDate = "pTravelToDate";	
	public static String tpFamilyMember = "pFamilyMember";
	public static String tpFamilyFlag = "pFamilyFlag";
	public static String pFamilyList = "pFamilyList";
	public static String tpLoading = "pLoading";	
	public static String tpDiscount = "pDiscount";
	public static String tpSpecialDiscount = "pSpecialDiscount";
	public static String tpCommercialDisc = "pCommercialDisc";	
	public static String tpCommDiscRate = "pCommDiscRate";
	public static String tpDateOfBirth = "pDateOfBirth";
	public static String tpPassengerName = "pPassengerName";	
	
	
	public static String genTravalQuotParamaterMETHOD= "genTravalQuotParamater";
	public static String trImeiNo = "pImeiNo";
	public static String trpCustomerName = "pCustomerName";
	public static String trpUpdatedBy = "pUpdatedBy";	
	public static String trpParamList = "pParamList";
	public static String trpFamilyList = "pFamilyList";
}
