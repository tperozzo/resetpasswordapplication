package com.example.resetpasswordapplication;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class DeviceAdminReceiver extends  android.app.admin.DeviceAdminReceiver {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
    }


    public static void enableAdmin(AppCompatActivity appCompatActivity){
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component(appCompatActivity));
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, appCompatActivity.getString(R.string.app_name));
        appCompatActivity.startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    public static ComponentName component(Context context) {
        return new ComponentName(context, DeviceAdminReceiver.class);
    }

}
