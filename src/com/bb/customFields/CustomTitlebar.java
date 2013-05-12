package com.bb.customFields;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import com.bb.constants.Constants;

public class CustomTitlebar extends Field {
	private Bitmap image;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		invalidate();
	}

	private int foregroundColor;
	private int backgroundColor;

	public CustomTitlebar(String label, int foregroundColor,
			int backgroundColor, Bitmap image, long style) {
		super(style);
		this.title = label;
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.image = image;
	}

	public CustomTitlebar(String label, int foregroundColor,
			int backgroundColor, long style) {
		super(style);
		this.title = label;
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
	}

	protected void layout(int width, int height) {
		if ((getStyle() & Field.USE_ALL_WIDTH) == Field.USE_ALL_WIDTH) {
			setExtent(width, Math.min(height, getPreferredHeight()));
		} else {
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
	}

	protected void paint(Graphics graphics) {
		graphics.setBackgroundColor(backgroundColor);
		graphics.clear();
		graphics.setColor(foregroundColor);
		//graphics.setFont(Constants.font);
		if (image != null) {
			Font font = graphics.getFont();
			int textY = (getHeight() - getFont().getHeight()) / 2;
			int imageY = (getHeight() - image.getHeight()) / 2;
			graphics.drawBitmap(0, imageY, image.getWidth(), image.getHeight(),
					image, 0, 0);
			graphics.drawText(title, getWidth() - font.getAdvance(title),
					textY, DrawStyle.ELLIPSIS, getWidth() - image.getWidth());
		} else {
			graphics.drawText(title, 0, 0, DrawStyle.ELLIPSIS, getWidth());
		}
	}

	public int getPreferredHeight() {
		if (image != null) {
			return Math.max(getFont().getHeight(), image.getHeight());
		} else {
			return getFont().getHeight();
		}
	}

	public int getPreferredWidth() {
		int width = getFont().getAdvance(title);
		if (image != null) {
			width += image.getWidth();
		}
		return width;
	}
}
