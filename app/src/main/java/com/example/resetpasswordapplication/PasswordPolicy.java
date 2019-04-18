package com.example.resetpasswordapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordPolicy {

    private static final String PREFS_NAME = "password-token";
    private static final String TOKEN_NAME = "token";
    private static final int REQUEST_CONFIRM_CREDENTIAL = 1;

    private static KeyguardManager mKeyguardMgr;

    @TargetApi(Build.VERSION_CODES.M)
    public static void resetPassword(Activity activity, String password) {
        mKeyguardMgr = activity.getSystemService(KeyguardManager.class);
        ComponentName component = DeviceAdminReceiver.component(activity);
        DevicePolicyManager policyManager = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        policyManager.setPasswordQuality(component, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        policyManager.setPasswordMinimumLength(component, 0);
        boolean success = false;
        boolean active;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byte[] token = loadPasswordResetTokenFromPreference(activity);
            try {
                password = "";
                if(token == null) token = generateRandomPasswordToken();

                policyManager.setResetPasswordToken(DeviceAdminReceiver.component(activity), token);


                success = policyManager.resetPasswordWithToken(DeviceAdminReceiver.component(activity), password, token, 0);
                if(success) savePasswordResetTokenToPreference(activity, token);


                active = policyManager.isResetPasswordTokenActive(DeviceAdminReceiver.component(activity));
                if(!active) activatePasswordToken(activity);




            }catch (Exception e){
                Log.v(PasswordPolicy.class.getName() , e.getMessage());
            }
        }else{
            success = policyManager.resetPassword(password, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        }
        Log.d("Device password changed", String.valueOf(success));
    }

    public static byte[] generateRandomPasswordToken() {
        try {
            return SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
        } catch (NoSuchAlgorithmException e) {
            Log.v(PasswordPolicy.class.getName() , e.getMessage());
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void savePasswordResetTokenToPreference(Context context, byte[] token) {
        Context directBootContext = context.createDeviceProtectedStorageContext();

        SharedPreferences settings = directBootContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (token != null) {
            editor.putString(TOKEN_NAME, Base64.getEncoder().encodeToString((token)));
        } else {
            editor.remove(TOKEN_NAME);
        }
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static byte[] loadPasswordResetTokenFromPreference(Context context) {
        Context directBootContext = context.createDeviceProtectedStorageContext();
        SharedPreferences settings = directBootContext.getSharedPreferences(PREFS_NAME, 0);
        String tokenString = settings.getString(TOKEN_NAME, null);
        if (tokenString != null) {
            return Base64.getDecoder().decode(tokenString.getBytes(StandardCharsets.UTF_8));
        } else {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void activatePasswordToken(Activity activity) {
        Intent intent = mKeyguardMgr.createConfirmDeviceCredentialIntent(null, null);
        if (intent != null) {
            activity.startActivityForResult(intent, REQUEST_CONFIRM_CREDENTIAL);
        }
    }


}
