package com.project.adtracker;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by KyungRyun on 2014-12-31.
 */
public class SaveUUID {
    public String getDevicesUUID(Context context){

        final TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        final String mDevice, mSerial, androidID;

        mDevice = ""+tm.getDeviceId();
        mSerial = ""+tm.getSimSerialNumber();
        androidID = ""+ android.provider.Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        UUID deviceUUID = new UUID(androidID.hashCode(), ((long)mDevice.hashCode()<<32)| mSerial.hashCode());
        return deviceUUID.toString();
    }
}
