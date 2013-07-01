package de.dst.xposed.usbdebugginglistenercontrol;

import android.util.Log;
import de.robv.android.xposed.XposedBridge;
public class LogUtil {
	private static final String TAG = "USBDebuggingListenerControl";
	private static final boolean DEBUG_ENABLED = true;


	public static void logDebug(String msg, boolean logExposed, boolean logAndroid) {
		if(!DEBUG_ENABLED) {
			return;
		}
		if(logExposed) {
			XposedBridge.log("D:" + TAG + ": " + msg);
		}
		if(logAndroid) {
			Log.d(TAG, msg);
		}
	}
	
	public static void logError(String msg, boolean logExposed, boolean logAndroid) {
		if(logExposed) {
			XposedBridge.log("E:" + TAG + ": " + msg);
		}
		if(logAndroid) {
			Log.e(TAG, msg);
		}
	}

	public static void logError(String msg, Throwable th, boolean logExposed, boolean logAndroid) {
		if(logExposed) {
			XposedBridge.log("E:" + TAG + ": " + msg);
			XposedBridge.log(th);
		}
		if(logAndroid) {
			Log.e(TAG, msg, th);
		}
	}
}
