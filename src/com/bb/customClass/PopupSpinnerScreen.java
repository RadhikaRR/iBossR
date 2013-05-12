package com.bb.customClass;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.bb.customFields.SpinnerField;

public class PopupSpinnerScreen extends PopupScreen {

	private VerticalFieldManager verticalFieldManager;

	public PopupSpinnerScreen(String info) {

		super(new HorizontalFieldManager());

		//for Transparent Background with black border
		// Background bg = BackgroundFactory.createSolidTransparentBackground(Color.BLACK, 200);
		// this.setBackground(bg);

		Bitmap borderBitmap = Bitmap.getBitmapResource("BlackTrans.png");
		XYEdges padding = new XYEdges(12, 12, 12, 12);
		Border roundedBorder = BorderFactory.createBitmapBorder(padding, borderBitmap);
		this.setBorder(roundedBorder);

		verticalFieldManager = new VerticalFieldManager(FIELD_HCENTER | FIELD_VCENTER);

		if (!(info.equals("") || info.equals(null))) {
			verticalFieldManager.add(new LabelField(info, FIELD_HCENTER));
			verticalFieldManager.add(new LabelField());
		}

		verticalFieldManager.add(new SpinnerField());

		add(verticalFieldManager);
	}
}