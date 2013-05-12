package com.bb.persistDB;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.system.ControlledAccessException;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.bb.customClass.PopupSpinnerScreen;


public class PersistProvider {
	private PersistentObject persistentObject;
	private static long databaseKey = 0x1b4ae5ce93ae8808L;

	public static PersistProvider INSTANCE = new PersistProvider();

	private PersistProvider() {

	}

	public void saveObject(String key, String value) {

		System.out.println(key + "  " + value);
		try {
			if (key == null || value == null) {
				return;
			}
			persistentObject = PersistentStore.getPersistentObject(databaseKey);
			synchronized (persistentObject) {
				PersistImpl contents = (PersistImpl) persistentObject
						.getContents();
				if (contents == null) {
					contents = new PersistImpl();
				}
				contents.put(key.trim(), value.trim());
				persistentObject.setContents(contents);
				persistentObject.commit();
			}
		} catch (ControlledAccessException e) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication()
							.getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Dialog.alert("Can not access Persistent Store");
				}
			});
			
		} catch (final Exception e) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Screen screen = UiApplication.getUiApplication()
							.getActiveScreen();
					if (screen instanceof PopupSpinnerScreen) {
						UiApplication.getUiApplication().popScreen(screen);
					}
					Dialog.alert(e.getMessage());
				}
			});			
		}
	}

	public String getObject(String key) {
		if (key == null) {
			return null;
		}
		try {
			persistentObject = PersistentStore.getPersistentObject(databaseKey);
			synchronized (persistentObject) {
				PersistImpl value = (PersistImpl) persistentObject
						.getContents();
				if (value == null) {
					value = new PersistImpl();
				}
				return (String) value.get(key);
			}
		} catch (ControlledAccessException e) {
			Dialog.alert("Can not access Persistent Store");
		} catch (Exception e) {

		}
		return null;
	}

	public void saveObjects(Hashtable objects) {
		try {

			persistentObject = PersistentStore.getPersistentObject(databaseKey);
			synchronized (persistentObject) {
				PersistImpl contents = (PersistImpl) persistentObject
						.getContents();
				if (contents == null) {
					contents = new PersistImpl();
				}

				Enumeration keys = objects.keys();
				while (keys.hasMoreElements()) {
					Object key = (Object) keys.nextElement();
					contents.put(key, objects.get(key));
				}
				persistentObject.setContents(contents);
				persistentObject.commit();
			}
		} catch (ControlledAccessException e) {
			Dialog.alert("Can not access Persistent Store");
		} catch (Exception e) {

		}
	}

	public void removeObject(String key) {
		if (key == null) {
			return;
		}
		try {
			persistentObject = PersistentStore.getPersistentObject(databaseKey);
			synchronized (persistentObject) {
				PersistImpl value = (PersistImpl) persistentObject
						.getContents();
				if (value != null) {
					value.remove(key);
				}
				persistentObject.setContents(value);
				persistentObject.commit();
			}
		} catch (ControlledAccessException e) {
			Dialog.alert("Can not access Persistent Store");
		} catch (Exception e) {

		}
	}
}
