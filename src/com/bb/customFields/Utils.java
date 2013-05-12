package com.bb.customFields;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

public class Utils {

	private static Bitmap backGroundImage = null;

	/*
	 * sets opacity.
	 * 
	 * @param image @param width @param height
	 */
	public static Bitmap initBackgroundImage(int width, int height) {

		if (backGroundImage != null) {
			return backGroundImage;
		}
		try {
			backGroundImage = Bitmap.getBitmapResource("tile.png");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (backGroundImage == null) {
			return null;
		}
		int[] imageRGB = new int[backGroundImage.getHeight()
				* backGroundImage.getWidth()];
		backGroundImage.getARGB(imageRGB, 0, backGroundImage.getWidth(), 0, 0,
				backGroundImage.getWidth(), backGroundImage.getHeight());

		int[] rescaledImageRGB = reescalaArray(imageRGB, backGroundImage
				.getWidth(), backGroundImage.getHeight(), width, height);
		imageRGB = null;

		int[] processed = blend(rescaledImageRGB, 255);
		imageRGB = null;
		backGroundImage = new Bitmap(width, height);
		backGroundImage.setARGB(processed, 0, backGroundImage.getWidth(), 0, 0,
				backGroundImage.getWidth(), backGroundImage.getHeight());

		return backGroundImage;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h,
			boolean keepHeight) {
		if (bitmap == null)
			return null;
		int sourceWidth = bitmap.getWidth();
		int sourceHeight = bitmap.getHeight();
		double ratio = (double) sourceHeight / (double) sourceWidth;

		int width = w;
		int height = (int) (width * ratio);

		if (!keepHeight) {
			if (height >= h) {
				height = h;
				width = (int) (height / ratio);
			}
		}
		if (bitmap.getWidth() == width && bitmap.getHeight() == height) {
			return bitmap;
		}

		int[] imageRGB = new int[bitmap.getHeight() * bitmap.getWidth()];
		bitmap.getARGB(imageRGB, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(),
				bitmap.getHeight());
		int[] rescaledImageRGB = reescalaArray(imageRGB, bitmap.getWidth(),
				bitmap.getHeight(), width, height);
		imageRGB = null;

		bitmap = new Bitmap(width, height);
		bitmap.setARGB(rescaledImageRGB, 0, bitmap.getWidth(), 0, 0, bitmap
				.getWidth(), bitmap.getHeight());

		return bitmap;
	}

	synchronized private static int[] reescalaArray(int[] rgb, int x, int y,
			int x2, int y2) {

		int out[] = new int[x2 * y2];

		for (int yy = 0; yy < y2; yy++) {

			int dy = yy * y / y2;

			for (int xx = 0; xx < x2; xx++) {

				int dx = xx * x / x2;

				out[(x2 * yy) + xx] = rgb[(x * dy) + dx];
			}
		}
		rgb = null;
		return out;
	}

	/**
	 * This method takes rgb array of image and blend it with some alpha value
	 * to make image transparent.
	 * 
	 * @param raw
	 *            is the rgb array of image.
	 * @param alphaValue
	 *            can be in between 0 and 255. lower value will make image more
	 *            transparent.
	 * @return new rgb array that can be used to create new image.
	 */
	synchronized public static int[] blend(int[] raw, int alphaValue) {
		int len = raw.length;
		if (alphaValue > 254)
			return raw;

		// Start loop through all the pixels in the image.
		for (int i = 0; i < len; i++) {
			int a = 0;
			// a = alphaValue; // set the alpha value we want to use 0-255.
			// If raw[i] == -1 means white then it will be purely transparent
			// this is for making edges of list element purely transparent.
			if (raw[i] != -1)
				a = alphaValue;
			int color = (raw[i] & 0x00FFFFFF); // get the color of the pixel.

			a = (a << 24); // left shift the alpha value 24 bits.
			// if color = 00000000 11111111 11111111 00000000 (0xFFFF00 =
			// Yellow)
			// and alpha= 01111111 00000000 00000000 00000000
			// then c+a = 01111111 11111111 11111111 00000000
			// and the pixel will be blended.
			color += a;
			raw[i] = color;
		}
		return raw;
	}

	public static Bitmap resizeImageToDeviceSize(String imagename, int width,
			int height) {

		EncodedImage image = EncodedImage.getEncodedImageResource(imagename);

		int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
		int currentHeightFixed32 = Fixed32.toFP(image.getHeight());

		// int width = image.getWidth() /2;
		// int height = image.getHeight() /2;

		int requiredWidthFixed32 = Fixed32.toFP(width);
		int requiredHeightFixed32 = Fixed32.toFP(height);

		int scaleXFixed32 = Fixed32.div(currentWidthFixed32,
				requiredWidthFixed32);
		int scaleYFixed32 = Fixed32.div(currentHeightFixed32,
				requiredHeightFixed32);

		image = image.scaleImage32(scaleXFixed32, scaleYFixed32);

		return image.getBitmap();
	}
}
