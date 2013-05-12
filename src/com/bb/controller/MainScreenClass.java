package com.bb.controller;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.bb.customFields.BitmapButtonField;
import com.bb.customFields.CustomTitlebar;

public class MainScreenClass extends MainScreen {

	protected Border roundedBorder;
	public static VerticalFieldManager mainVFM;
	BitmapButtonField home, exit, menu, info;
	
	public static VerticalFieldManager vfm;

	public MainScreenClass() {

		super(NO_VERTICAL_SCROLL | NO_VERTICAL_SCROLLBAR);

		Bitmap borderBitmap = Bitmap.getBitmapResource("rounded-border.png");
		XYEdges padding = new XYEdges(12, 12, 12, 12);
		roundedBorder = BorderFactory.createBitmapBorder(padding, borderBitmap);

		this.getMainManager().setBackground(
				BackgroundFactory.createLinearGradientBackground(0x00FFFFFF, 0x00FFFFFF, 0x00219AF7, 0x0095C9F8));

		vfm = new VerticalFieldManager(USE_ALL_WIDTH);
		Bitmap titleBitmap = Bitmap.getBitmapResource("logo.PNG");
		CustomTitlebar titleBarField = new CustomTitlebar("", Color.WHITE, 0x000072BC, titleBitmap, Field.USE_ALL_WIDTH);
		vfm.add(titleBarField);
		setTitle(vfm);

		Bitmap homeBitmap = Bitmap.getBitmapResource("home_small.png");
		Bitmap homeFocBitmap = Bitmap.getBitmapResource("home_big.png");

		Bitmap menuBitmap = Bitmap.getBitmapResource("menu_small.png");
		Bitmap menuFocBitmap = Bitmap.getBitmapResource("menu_big.png");
		
		Bitmap exitBitmap = Bitmap.getBitmapResource("exit_small.png");
		Bitmap exitFocBitmap = Bitmap.getBitmapResource("exit_big.png");

		Bitmap infoBitmap = Bitmap.getBitmapResource("info_small.png");
		Bitmap infoFocBitmap = Bitmap.getBitmapResource("info_big.png");

		home = new BitmapButtonField(homeBitmap, homeFocBitmap, DrawStyle.HCENTER);
		menu = new BitmapButtonField(menuBitmap, menuFocBitmap, DrawStyle.HCENTER);		
		exit = new BitmapButtonField(exitBitmap, exitFocBitmap, DrawStyle.HCENTER);
		info = new BitmapButtonField(infoBitmap, infoFocBitmap, DrawStyle.HCENTER);

		mainVFM = new VerticalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR) {
			protected void sublayout(int maxWidth, int maxHeight) {
				super.sublayout(Display.getWidth(), Display.getHeight());

				if (Display.getWidth() == 320) {
					setExtent(Display.getWidth(), 142);
				} else if (Display.getWidth() == 360) {
					setExtent(Display.getWidth(), 380);
				} else if (Display.getWidth() == 640) {
					setExtent(Display.getWidth(), 293);
				} else {
					setExtent(480, (Display.getHeight() - home.getHeight())*75/100);
				}
			}
		};
//		mainVFM.setBorder(roundedBorder);
		add(mainVFM);

		CustomIngredientsButton ingrBtnLayout = new CustomIngredientsButton();
		add(ingrBtnLayout);
	}

	class CustomIngredientsButton extends Manager {
		protected CustomIngredientsButton() {
			super(Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL);
			
			add(home);
			add(menu);
			add(exit);
			add(info);
		}

		protected void sublayout(int width, int height) {
			layoutChild(home, Display.getWidth(), Display.getHeight());
			setPositionChild(home, (Display.getWidth() - home.getWidth()) * 30 / 100, 0);

			layoutChild(menu, Display.getWidth(), Display.getHeight());
			setPositionChild(menu, (Display.getWidth() - menu.getWidth()) * 44 / 100, 0);

			layoutChild(exit, Display.getWidth(), Display.getHeight());
			setPositionChild(exit, (Display.getWidth() - exit.getWidth()) * 58 / 100, 0);

			layoutChild(info, Display.getWidth(), Display.getHeight());
			setPositionChild(info, (Display.getWidth() - info.getWidth()) * 72 / 100, 0);

			setExtent(455, home.getHeight());
		}
	}
}
