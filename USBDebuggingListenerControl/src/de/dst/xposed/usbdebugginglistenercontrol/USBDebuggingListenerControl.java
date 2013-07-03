package de.dst.xposed.usbdebugginglistenercontrol;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.SharedPreferences;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class USBDebuggingListenerControl implements IXposedHookZygoteInit, IXposedHookLoadPackage {

	public static final String MY_PACKAGE_NAME = USBDebuggingListenerControl.class.getPackage().getName();
	public static final String PREFS = "USBDebuggingListenerControlSettings";
	public static final String PREF_ListenerEnabled = "ListenerEnabled";
	
	/* 
	 * Thanks to OXINARF (http://forum.xda-developers.com/member.php?u=4670128)
	 * for helping me with the right package to use.
	 */
	private static final String PACKAGE_NAME = "android";

	private SharedPreferences prefs;

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		LogUtil.logDebug("Starting up and getting preferences", true, false);
		prefs = AndroidAppHelper.getSharedPreferencesForPackage(MY_PACKAGE_NAME, PREFS, Context.MODE_PRIVATE);
		LogUtil.logDebug(prefs != null ? "Preferences loaded" : "Preferences not loaded", true, true);
	}

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (lpparam.packageName.equals(PACKAGE_NAME)) {
			XposedBridge.log("Package " + PACKAGE_NAME + " found");
			try {
				//Change to preference only takes effect when this is called here
				AndroidAppHelper.reloadSharedPreferencesIfNeeded(prefs);
				LogUtil.logDebug("Reload SharedPreferences", true, false);
				
				boolean listenerEnabled = prefs.getBoolean(PREF_ListenerEnabled, true);
				LogUtil.logDebug(listenerEnabled ? "Listener is enabled" : "Listener is disabled", true, false);
				if(!listenerEnabled) {
//					XC_MethodHook.Unhook methode = XposedHelpers.findAndHookMethod("com.android.server.usb.UsbDebuggingManager", lpparam.classLoader, "listenToSocket", XC_MethodReplacement.DO_NOTHING);
					// Thanks to Tungstwenty (http://forum.xda-developers.com/member.php?u=4322181)
					XC_MethodHook.Unhook methode = XposedHelpers.findAndHookMethod("com.android.server.usb.UsbDebuggingManager", lpparam.classLoader, "run", XC_MethodReplacement.DO_NOTHING);
					LogUtil.logDebug(methode != null ? "Methode found" : "Methode not found", true, false);
				}	
			}
			catch (Throwable t) { 
				LogUtil.logError("Error during disable of Methode: com.android.server.usb.UsbDebuggingManager.UsbDebuggingManager.listenToSocket()", t, true, true); 
			}
		} else {
//			LogUtil.logDebug(lpparam.packageName, true, true);
		}
	}

}
