package com.bb.sms;

import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

import net.rim.device.api.system.CDMAInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.SIMCardInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.bb.constants.Constants;
import com.bb.persistDB.PersistProvider;

public class MeSMSSender {

	public static MeSMSSender meSMSSender = new MeSMSSender();
	private String imei;
	private String imsi;

	public void sendSMSMessage(final String number) {

		final String imeiNoAndimsi = getMessageText();

		Thread thread = new Thread() {
			public void run() {
				MessageConnection smsconn = null;
				try {
					String address = "sms://" + number;

					smsconn = (MessageConnection) Connector.open(address);
					TextMessage txtmessage = (TextMessage) smsconn
							.newMessage(MessageConnection.TEXT_MESSAGE);
					txtmessage.setAddress(address);
					txtmessage.setPayloadText("MOBZON |" + imeiNoAndimsi);
					smsconn.send(txtmessage);
					System.out.println("message-------------------"+txtmessage);
					PersistProvider.INSTANCE.saveObject(Constants.SMSFLAG,"true");
				} catch (Exception e) {
					synchronized (UiApplication.getEventLock()) {
						Dialog.alert("Sms Failed: " + e.getMessage() + " "
								+ e.getClass().getName());
					}
				} finally {
					try {
						smsconn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		};
		thread.start();
	}

	private String getMessageText() {

		if (RadioInfo.getNetworkType() == RadioInfo.NETWORK_CDMA) {
			try {
				imei = Integer.toString(CDMAInfo.getESN());
					
				imsi = GPRSInfo.imeiToString(CDMAInfo.getIMSI());
			} catch (Exception e) {
				synchronized (UiApplication.getEventLock()) {
					Dialog.alert("Failed to retrive IMEI or IMSI:" + e.getMessage());
				}
			}

		} else {
			try {
				imei = GPRSInfo.imeiToString(GPRSInfo.getIMEI(), false);
					
				imsi = GPRSInfo.imeiToString(SIMCardInfo.getIMSI());
			} catch (Exception e) {
				synchronized (UiApplication.getEventLock()) {
					Dialog.alert("Failed to retrive IMEI or IMSI:" + e.getMessage());
				}
			}
		}
		return imei+"|"+imsi;
	}
}
