package com.bb.customClass;

import net.rim.device.api.system.CDMAInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class ImeiClass {

	public static ImeiClass INSTANCE = new ImeiClass();
	private String IMEI;
	
	public ImeiClass() {
		
	}

	public String getIMEINumber() {

		if (RadioInfo.getNetworkType() == RadioInfo.NETWORK_CDMA) {
			try {
				IMEI = Integer.toString(CDMAInfo.getESN());
			} catch (Exception e) {
				synchronized (UiApplication.getEventLock()) {
					Dialog.alert("Failed to retrive IMEI for Authentication:"
							+ e.getMessage());
				}
			}
		} else {
			try {
				IMEI = GPRSInfo.imeiToString(GPRSInfo.getIMEI(), false);
			} catch (Exception e) {
				synchronized (UiApplication.getEventLock()) {
					Dialog.alert("Failed to retrive IMEI for Authentication:"
							+ e.getMessage());
				}
			}
		}
		return IMEI;
	}
}
