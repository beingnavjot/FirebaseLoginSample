package com.firebaseotplogin.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.gson.Gson



class Util(val context: Context) {
    var TAG = Util::class.java.name

    fun <T : Any> convertJsonToObject(json: String, classOnject: Class<T>): T {
        val gson = Gson()
        val classData = gson.fromJson(json, classOnject) as T
        return classData
    }

    @SuppressLint("HardwareIds")
    fun generateUniqueID(): String {
        val uniqueAndroidID = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        Log.e(TAG, uniqueAndroidID)
        return uniqueAndroidID
    }
}