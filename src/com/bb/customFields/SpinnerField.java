package com.bb.customFields;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class SpinnerField extends Field {

	private static Bitmap spinnerBitmap;
	private Timer spinnerTimer;
	private int count = 0;
	private static int widthOfPart;

	static {
		spinnerBitmap = Bitmap.getBitmapResource("spinner.png");
		widthOfPart = spinnerBitmap.getWidth() / 12;
	}

	public SpinnerField() {
		super(FIELD_HCENTER);
	}

	protected void layout(int width, int height) {
		setExtent(widthOfPart, spinnerBitmap.getHeight());
	}

	protected void paint(Graphics graphics) {
		XYRect extent = graphics.getClippingRect();
		graphics.drawBitmap(extent, spinnerBitmap, (widthOfPart * count), 0);
	}

	protected void onDisplay() {
		super.onDisplay();
		startTimer();
	}

	protected void onUndisplay() {
		stopTimer();
		super.onUndisplay();
	}

	private void startTimer() {
		if (spinnerTimer == null) {
			spinnerTimer = new Timer();
			spinnerTimer.schedule(new TimerTaskImpl(), 0, 100);
		}
	}

	private void stopTimer() {
		if (spinnerTimer != null) {
			spinnerTimer.cancel();
		}
	}

	class TimerTaskImpl extends TimerTask {
		public void run() {
			count++;
			if (count > 11) {
				count = 0;
			}
			SpinnerField.this.invalidate();
		}
	}
}
