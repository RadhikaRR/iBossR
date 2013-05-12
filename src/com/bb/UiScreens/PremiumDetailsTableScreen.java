package com.bb.UiScreens;

import java.util.Hashtable;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.controller.MainScreenClass;
import com.bb.customClass.PopupSpinnerScreen;
import com.bb.customFields.BorderLabelFieldImpl;
import com.bb.customFields.StringSplitter;
import com.bb.webService.CallWebService;

public class PremiumDetailsTableScreen extends MainScreenClass {

	GridFieldManager grid1, grid2;
	BorderLabelFieldImpl label;
	EditField assCoverEdit;

	String IDV;

	String[] lab;
	String[] val;

	public PremiumDetailsTableScreen(final Hashtable hashtable, final String pCalculatedIDV, final String pImeiNo,
			final String pVecTypeCode, final String pVecMakeCode, final String pVecModel, final String pVecSubType,
			final String pPinCode, final String pdate, final String pVecNonElec, final String pVecElec,
			final String pVecCng, final String pSumAssured, final String pCalculatedIDVv, final String pcommercialdisc,
			final String pdriveassure, final String pncbValue, final String pBussType, final String pUserName) {

		VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.HORIZONTAL_SCROLL);
		vfm.setMargin(5, 0, 5, 10);

		lab = new String[4];
		val = new String[4];

		for (int k = 0; k < 4; k++) {
			String sss = (String) hashtable.get("stringval" + (k + 3));
			String st = sss.substring(1);

			String[] q = StringSplitter.INSTANCE.split(st, "#");
			lab[k] = q[0];
			val[k] = q[1];
		}

		GridTable1(hashtable, pCalculatedIDV);
		GridTable2(hashtable);

		VerticalFieldManager gridHorizontalFieldManager1 = new VerticalFieldManager(Manager.HORIZONTAL_SCROLL
				| Manager.VERTICAL_SCROLL);
		gridHorizontalFieldManager1.add(grid1);
		vfm.add(gridHorizontalFieldManager1);

		VerticalFieldManager gridHorizontalFieldManager2 = new VerticalFieldManager(Manager.HORIZONTAL_SCROLL
				| Manager.VERTICAL_SCROLL);
		gridHorizontalFieldManager2.add(grid2);
		vfm.add(gridHorizontalFieldManager2);

		ButtonField btnReCalPrem = new ButtonField("Re-Calculate Premium", FIELD_HCENTER);
		btnReCalPrem.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				System.out.println("");
				IDV = assCoverEdit.getText();
				if (IDV.equals(null) || IDV.equals("")) {
					IDV = pCalculatedIDV;
				}
				Controller.showScreen(new PopupSpinnerScreen("Processing..."));
				Thread thread = new Thread() {
					public void run() {
						boolean flag = true;
						flag = CallWebService.INSTANCE.calculatePremiumWS(Constants.IMEI, pVecTypeCode, pVecMakeCode,
								pVecModel, pVecSubType, pPinCode, pdate, pVecNonElec, pVecElec, pVecCng, pSumAssured,
								IDV, pcommercialdisc, pdriveassure, pncbValue, pBussType, pUserName);
						while (flag == false) {
							flag = CallWebService.INSTANCE.calculatePremiumWS(Constants.IMEI, pVecTypeCode,
									pVecMakeCode, pVecModel, pVecSubType, pPinCode, pdate, pVecNonElec, pVecElec,
									pVecCng, pSumAssured, IDV, pcommercialdisc, pdriveassure, pncbValue, pBussType,
									pUserName);
						}
						System.out.println("");
					}
				};
				thread.start();
			}
		});
		vfm.add(btnReCalPrem);

		ButtonField btn = new ButtonField("Generate Reference No.", FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				String stringval7 = (String) hashtable.get("stringval7");
				String vall = val[0];
				Controller.showScreen(new GenerateQuotationScreen(vall, stringval7));
				return true;
			}
		};
		btn.setMargin(5, 0, 5, 0);
		vfm.add(btn);

		mainVFM.add(vfm);
	}

	public void GridTable1(Hashtable hashtable, String pCalculatedIDV) {

		String str1 = (String) hashtable.get("stringval1");

		String[] a = StringSplitter.INSTANCE.split(str1, "#M#");

		String[] label1 = new String[a.length];
		String[] value1 = new String[a.length];

		grid1 = new GridFieldManager(a.length + 3, 2, GridFieldManager.FIXED_SIZE);
		grid1.setColumnProperty(0, GridFieldManager.FIXED_SIZE, Display.getWidth() - (Display.getWidth() / 4));
		grid1.setColumnProperty(1, GridFieldManager.FIXED_SIZE, 95);

		label = new BorderLabelFieldImpl("SECTION", Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.fontBold6);
		label.setFontColor(Color.WHITE);
		label.setBgColor(0x000072BC);
		grid1.add(label);

		label = new BorderLabelFieldImpl("PREMIUM", Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.fontBold6);
		label.setFontColor(Color.WHITE);
		label.setBgColor(0x000072BC);
		grid1.add(label);

		label = new BorderLabelFieldImpl("Calculated IDV", Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.fontBold6);
		label.setFontColor(Color.WHITE);
		label.setBgColor(0x000072BC);
		grid1.add(label);

		// label = new BorderLabelFieldImpl(pCalculatedIDV, Field.FOCUSABLE |
		// USE_ALL_WIDTH);
		assCoverEdit = new EditField("", pCalculatedIDV, 12, USE_ALL_WIDTH | Field.FOCUSABLE | EditField.FILTER_NUMERIC) {
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
		assCoverEdit.setFont(Constants.font);
		grid1.add(assCoverEdit);

		BorderLabelFieldImpl labelOwnDamage = new BorderLabelFieldImpl("A. Own Damage", Field.FOCUSABLE | USE_ALL_WIDTH);
		labelOwnDamage.setFont(Constants.fontBold6);
		labelOwnDamage.setFontColor(Color.WHITE);
		labelOwnDamage.setBgColor(0x000072BC);
		grid1.add(labelOwnDamage);

		BorderLabelFieldImpl labelOwnDamage1 = new BorderLabelFieldImpl("", USE_ALL_WIDTH);
		labelOwnDamage1.setFont(Constants.fontBold6);
		labelOwnDamage1.setBgColor(0x000072BC);
		grid1.add(labelOwnDamage1);

		for (int i = 0; i < a.length; i++) {

			String[] b = StringSplitter.INSTANCE.split(a[i], "~");
			label1[i] = b[0];
			value1[i] = b[1];

			label = new BorderLabelFieldImpl(label1[i], Field.FOCUSABLE | USE_ALL_WIDTH);
			label.setFont(Constants.font);
			grid1.add(label);

			label = new BorderLabelFieldImpl(value1[i], Field.FOCUSABLE | USE_ALL_WIDTH);
			label.setFont(Constants.font);
			grid1.add(label);
		}
		System.out.println("");
	}

	public void GridTable2(Hashtable hashtable) {

		String str2 = (String) hashtable.get("stringval2");

		String[] c = StringSplitter.INSTANCE.split(str2, "#M#");

		String[] label2 = new String[c.length];
		String[] value2 = new String[c.length];

		grid2 = new GridFieldManager(c.length + 5, 2, GridFieldManager.FIXED_SIZE);
		grid2.setColumnProperty(0, GridFieldManager.FIXED_SIZE, Display.getWidth() - (Display.getWidth() / 4));
		grid2.setColumnProperty(1, GridFieldManager.FIXED_SIZE, 95);

		label = new BorderLabelFieldImpl("B. Basic Third Party Liability", Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.fontBold6);
		label.setFontColor(Color.WHITE);
		label.setBgColor(0x000072BC);
		grid2.add(label);

		label = new BorderLabelFieldImpl("PREMIUM", Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.fontBold6);
		label.setBgColor(0x000072BC);
		label.setFontColor(0x000072BC);
		grid2.add(label);

		for (int i = 0; i < c.length; i++) {

			String[] d = StringSplitter.INSTANCE.split(c[i], "~");
			label2[i] = d[0];
			value2[i] = d[1];

			label = new BorderLabelFieldImpl(label2[i], Field.FOCUSABLE | USE_ALL_WIDTH);
			label.setFont(Constants.font);
			grid2.add(label);

			label = new BorderLabelFieldImpl(value2[i], Field.FOCUSABLE | USE_ALL_WIDTH);
			label.setFont(Constants.font);
			grid2.add(label);
		}

		BorderLabelFieldImpl labelzero = new BorderLabelFieldImpl("C. " + lab[0], Field.FOCUSABLE | USE_ALL_WIDTH);
		labelzero.setFont(Constants.fontBold6);
		labelzero.setFontColor(Color.WHITE);
		labelzero.setBgColor(0x000072BC);
		grid2.add(labelzero);

		label = new BorderLabelFieldImpl(val[0], Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.font);
		grid2.add(label);

		BorderLabelFieldImpl labelone = new BorderLabelFieldImpl("D. " + lab[1], Field.FOCUSABLE | USE_ALL_WIDTH);
		labelone.setFont(Constants.fontBold6);
		labelone.setFontColor(Color.WHITE);
		labelone.setBgColor(0x000072BC);
		grid2.add(labelone);

		label = new BorderLabelFieldImpl(val[1], Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.font);
		grid2.add(label);

		BorderLabelFieldImpl labeltwo = new BorderLabelFieldImpl("E. " + lab[2], Field.FOCUSABLE | USE_ALL_WIDTH);
		labeltwo.setFont(Constants.fontBold6);
		labeltwo.setFontColor(Color.WHITE);
		labeltwo.setBgColor(0x000072BC);
		grid2.add(labeltwo);

		label = new BorderLabelFieldImpl(val[2], Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.font);
		grid2.add(label);

		BorderLabelFieldImpl labelthree = new BorderLabelFieldImpl("F. " + lab[3], Field.FOCUSABLE | USE_ALL_WIDTH);
		labelthree.setFont(Constants.fontBold6);
		labelthree.setFontColor(Color.WHITE);
		labelthree.setBgColor(0x000072BC);
		grid2.add(labelthree);

		label = new BorderLabelFieldImpl(val[3], Field.FOCUSABLE | USE_ALL_WIDTH);
		label.setFont(Constants.font);
		grid2.add(label);
		System.out.println("");
	}

	public boolean onClose() {

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
			}
		});
		return super.onClose();
	}

	public boolean onSavePrompt() {
		return true;
	}
}
