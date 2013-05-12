package com.bb.UiScreens;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;

import com.bb.constants.Constants;
import com.bb.controller.Controller;
import com.bb.controller.MainScreenClass;
import com.bb.customClass.CustomTransperentDialog;
import com.bb.customClass.PopupSpinnerScreen;
import com.bb.customFields.BorderLabelFieldImpl;
import com.bb.webService.CallWebService;

public class GenerateQuotationScreen extends MainScreenClass {

	BorderLabelFieldImpl b1;
	EditField nameEdit;

	public GenerateQuotationScreen(final String pNetPremium, final String stringval7) {

		b1 = new BorderLabelFieldImpl("Please enter your name", USE_ALL_WIDTH);
		b1.setFont(Constants.fontBold6);
		b1.setFontColor(Color.WHITE);
		b1.setBgColor(0x000072BC);
		b1.setMargin(5, 10, 0, 10);
		nameEdit = new EditField("", "", 50, FIELD_HCENTER | EDITABLE) {
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
		nameEdit.setMargin(10, 10, 10, 10);
		mainVFM.add(b1);
		mainVFM.add(nameEdit);

		ButtonField btn = new ButtonField("Generate", FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				if (nameEdit.getText() == null || nameEdit.getText().equals("")) {
					Controller.showScreen(new CustomTransperentDialog("Please enter Name"));
				} else {
					final String updatedBy = FirstScreen.pUpdatedBy;
					System.out.println("");
					Controller.showScreen(new PopupSpinnerScreen("Processing..."));
					Thread thread = new Thread() {
						public void run() {
							boolean flag = true;
							flag = CallWebService.INSTANCE.generateQuotationWS(Constants.IMEI, nameEdit.getText(),updatedBy,stringval7);
							while (flag == false) {
								flag = CallWebService.INSTANCE
										.generateQuotationWS(Constants.IMEI, nameEdit.getText(),updatedBy,stringval7);
							}
						}
					};
					thread.start();
				}
				return true;
			}
		};
		mainVFM.add(btn);
	}

	protected boolean onSavePrompt() {
		return true;
	}
	
	public boolean onClose() {
		// TODO Auto-generated method stub
		return true;
	}
}
