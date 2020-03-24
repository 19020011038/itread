package com.example.itread.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String TAG = "TAG";
    private static final String KET_LOGIN = "KEY_LOGIN";
    private static final String KEY_PICTURE = "KEY_PICTURE";
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferencesUtil mSharedPreferencesUtil;
    private final Context contex;
    private String accountId;

    public SharedPreferencesUtil(Context context) {
        this.contex = context.getApplicationContext();
        mPreferences = this.contex.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (mSharedPreferencesUtil == null) {
            mSharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return mSharedPreferencesUtil;
    }

    public boolean isLogin() {
        return getBoolean(KET_LOGIN, false);
    }


    public void setLogin(boolean value) {
        putBoolean(KET_LOGIN, value);
    }

    public boolean isPicture() { return getBoolean(KEY_PICTURE, false); }

    public void setPicture(boolean value) {
        putBoolean(KEY_PICTURE, value);
    }

    private void put(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    private void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    private String get(String key) {
        return mPreferences.getString(key, "");
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public void setAccountId(String accountId) {
        mEditor.putString("accountId", accountId);
        mEditor.apply();
    }

    public String getAccountId() {
        return mPreferences.getString("accountId", "");
    }

}