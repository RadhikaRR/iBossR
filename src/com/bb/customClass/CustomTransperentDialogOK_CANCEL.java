package com.bb.customClass;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class CustomTransperentDialogOK_CANCEL extends PopupScreen{

	private LabelField msgTextMessage;
	private ButtonField ok,cancel;
	
	public CustomTransperentDialogOK_CANCEL(String msg) {

		super(new VerticalFieldManager(), Field.FOCUSABLE);

		Bitmap borderBitmap = Bitmap.getBitmapResource("BlackTrans.png");
		XYEdges padding = new XYEdges(12, 12, 12, 12);
		Border roundedBorder = BorderFactory.createBitmapBorder(padding, borderBitmap);
		this.setBorder(roundedBorder);

		VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL);

		msgTextMessage = new LabelField(msg, Field.FIELD_HCENTER | Field.NON_FOCUSABLE);

		ok = new ButtonField("OK", Field.FIELD_HCENTER);
		ok.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == ok) {
					close();
				}
			}
		});	
		
		cancel = new ButtonField("CANCEL", Field.FIELD_HCENTER);
		cancel.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (field == cancel) {
					close();
				}
			}
		});	

		vfm.add(msgTextMessage);
		vfm.add(new LabelField());

		vfm.add(ok);
		vfm.add(cancel);
		
		add(vfm);
	}
}
