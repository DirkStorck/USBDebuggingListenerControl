package de.dst.xposed.usbdebugginglistenercontrol;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.SharedPreferences;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class USBDebuggingListenerControl implements IXposedHookZygoteInit, IXposedHookLoadPackage
{

	//Thanks to Tungstwenty for the preferences code, which I have taken from his Keyboard42DictInjector and made a bad job of it
	private static final String MY_PACKAGE_NAME = USBDebuggingListenerControl.class.getPackage().getName();

	public static final String PREFS = "USBDebuggingListenerControlSettings";
	public static final String PREF_ListenerEnabled = "ListenerEnabled";

	private SharedPreferences prefs;

	private static final String PACKAGE_NAME = "com.android.server.usb";


	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		prefs = AndroidAppHelper.getSharedPreferencesForPackage(MY_PACKAGE_NAME, PREFS, Context.MODE_PRIVATE);
	}

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (lpparam.packageName.equals(PACKAGE_NAME)) {
			XposedBridge.log("Package " + PACKAGE_NAME + " found");
			try {
				//Change to preference only takes effect when this is called here
				AndroidAppHelper.reloadSharedPreferencesIfNeeded(prefs);
				
				boolean listenerEnabled = prefs.getBoolean(PREF_ListenerEnabled, true);
				if(!listenerEnabled) {
					XposedBridge.log("Listener is disabled");
					XposedHelpers.findAndHookMethod(PACKAGE_NAME + ".UsbDebuggingManager", lpparam.classLoader, "listenToSocket", XC_MethodReplacement.DO_NOTHING);
				}	
			}
			catch (Throwable t) { 
				XposedBridge.log("Error during disable of Methode: " + PACKAGE_NAME + ".UsbDebuggingManager.listenToSocket()"); 
				XposedBridge.log(t); 
			}
		}
	}

}
