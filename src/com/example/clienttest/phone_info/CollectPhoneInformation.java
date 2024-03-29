package com.example.clienttest.phone_info;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class CollectPhoneInformation {

	Context mAppContext;
	public CollectPhoneInformation(Context mAppContext) {
		super();
		this.mAppContext = mAppContext;
	}

	public String getPhoneNum(){

//		 TelephonyManager tMgr =(TelephonyManager)mAppContext.getSystemService(Context.TELEPHONY_SERVICE);
		 TelephonyManager tMgr=(TelephonyManager)mAppContext.getSystemService(Context.TELEPHONY_SERVICE); 
		String mPhoneNumber = tMgr.getLine1Number();
		if (mPhoneNumber.length() ==0)
//			return "No available numner.";
			return tMgr.getDeviceId(); 
		else 
			return mPhoneNumber;
	}

	public String getMacAddress(){
		WifiManager wifiMan = (WifiManager) mAppContext.getSystemService(
                Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		String macAddr = wifiInf.getMacAddress();
		return macAddr;
	}
}
