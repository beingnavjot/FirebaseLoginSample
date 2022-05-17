package com.firebaseotplogin.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.*

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

open class CommonUtilities {
    companion object {
        var CATEGORY_VALUE = ""
        var INSURANCE_VALUE = ""
        var handler_value = ""

        @JvmStatic
        fun putInt(activity: Context, name: String?, value: Int) {
            val preferences: SharedPreferences =
                activity.getSharedPreferences(Constants.Borhan, Context.MODE_PRIVATE)
            preferences.edit().putInt(name, value).apply()
        }

        fun getInt(activity: Context, name: String?): Int {
            val preferences: SharedPreferences =
                activity.getSharedPreferences(Constants.Borhan, Context.MODE_PRIVATE)
            return preferences.getInt(name, 0)
        }


        fun putString(activity: Context, name: String?, value: String?) {
            val preferences: SharedPreferences =
                activity.getSharedPreferences(Constants.Borhan, Context.MODE_PRIVATE)
            preferences.edit().putString(name, value).apply()
        }

        fun getString(activity: Context, name: String?): String? {
            val preferences: SharedPreferences =
                activity.getSharedPreferences(Constants.Borhan, Context.MODE_PRIVATE)
            return preferences.getString(name, "")
        }


        fun putBoolean(activity: Context, name: String?, value: Boolean) {
            val preferences: SharedPreferences =
                activity.getSharedPreferences(Constants.Borhan, Context.MODE_PRIVATE)
            preferences.edit().putBoolean(name, value).apply()
        }

        fun getBoolean(activity: Context, name: String?): Boolean {
            val preferences: SharedPreferences =
                activity.getSharedPreferences(Constants.Borhan, Context.MODE_PRIVATE)
            return preferences.getBoolean(name, false)
        }

        fun clearPrefrences(activity: Context) {
            val preferences: SharedPreferences = activity.getSharedPreferences(
                Constants.Borhan, Context.MODE_PRIVATE
            )
            preferences.edit().clear().apply()
        }

        //get the current version number and name
        fun getVersionInfo(activity: Activity): String? {
            var versionName = ""
            var versionCode = -1
            try {
                val packageInfo =
                    activity.packageManager.getPackageInfo(activity.packageName, 0)
                versionName = packageInfo.versionName
                versionCode = packageInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            Log.e("versionName", "" + versionName)
            Log.e("versionCode", "" + versionCode)
            return versionName
        }


        fun printHashKey(pContext: Context) {
            try {
                val info = pContext.packageManager
                    .getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val hashKey = String(Base64.encode(md.digest(), 0))
                    Log.e("Login", "printHashKey() Hash Key: $hashKey")
                }
            } catch (e: NoSuchAlgorithmException) {
                Log.e("Login", "printHashKey()", e)
            } catch (e: Exception) {
                Log.e("Login", "printHashKey()", e)
            }
        }

/*        fun showToast(context: Context?, msg: String?) {
            //  Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

            SuperActivityToast.create(context!!, Style(), Style.TYPE_STANDARD)
                .setText(msg)
                .setTextColor(PaletteUtils2.getSolidColor(PaletteUtils2.WHITE))
                .setTextSize(Style.TEXTSIZE_SMALL)
                .setDuration(Style.DURATION_MEDIUM)
                //   .setHeight(155)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils2.getSolidColor(PaletteUtils2.BLACK))
                .setAnimations(Style.ANIMATIONS_POP).show()
        }

        fun showToast(context: Context?, msg: Int?) {
            if (msg != null) {
              //  Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                SuperActivityToast.create(context!!, Style(), Style.TYPE_STANDARD)
                    .setText(context.resources.getText(msg) as String?)
                    .setTextColor(PaletteUtils2.getSolidColor(PaletteUtils2.WHITE))
                    .setTextSize(Style.TEXTSIZE_SMALL)
                    .setDuration(Style.DURATION_MEDIUM)
                    //   .setHeight(155)
                    .setFrame(Style.FRAME_LOLLIPOP)
                    .setColor(PaletteUtils2.getSolidColor(PaletteUtils2.BLACK))
                    .setAnimations(Style.ANIMATIONS_POP).show()

            }
        }


        lateinit var dialog: Dialog
        fun showLoader(context: Context) {
            dialog = Dialogs.getLoadingDialog(context)
            dialog?.show()
        }

        fun hideLoader() {

            try {
                if (dialog.isShowing) {
                    dialog.cancel()
                    dialog.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }*/

        fun addZero(num: Int): String? {
            return if (num < 10) {
                "0$num"
            } else {
                "" + num
            }
        }


        fun changeDateFormat(
            dateStr: String?,
            oldFormat: String,
            newFormat: String
        ): String? {
            val inputFormat = SimpleDateFormat(oldFormat)
            val outputFormat = SimpleDateFormat(newFormat)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(dateStr)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()

            }
            return str
        }


        fun getDeviceToken(context: Context): String? {
            val android_id = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            Log.e("android_id", "" + android_id)
            return android_id
        }

        fun getFilePath(
            selectedImage: Uri?,
            context: Context
        ): String? {
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor =
                context.contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            println("File path $filePath")
            return filePath
        }

        fun isConnectingToInternet(context: Context?): Boolean? {
            val connectivity =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null) for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
            return false
        }


        fun changeStatusBarColor(activity: Activity, color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = color
            }
        }


        fun convertUTCtoLocal(createdAt: String?): String {
            var formatLong: SimpleDateFormat? = null
            var formatShort: SimpleDateFormat? = null
            var timeLong = ""
            try {
                timeLong = createdAt!!
                formatLong = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                formatShort = SimpleDateFormat("hh:mm aa", Locale.getDefault())
                Log.e("out", "time final: " + formatShort.format(formatLong.parse(timeLong)))


            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return formatShort!!.format(formatLong!!.parse(timeLong))
        }








        fun isMarshmallow(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        fun isLollipop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

        fun isOreo(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        }




        enum class IdType(val mId: Int) {
            NA(0), Artist(1), Album(2), Playlist(3);

            companion object {
                fun getTypeById(id: Int): IdType {
                    for (type in IdType.values()) {
                        if (type.mId == id) {
                            return type
                        }
                    }
                    throw IllegalArgumentException("Unrecognized id: $id")
                }
            }
        }

        fun isJellyBeanMR2(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
        }

        fun isJellyBean(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        }

        fun isJellyBeanMR1(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
        }

        fun milliSecondsToTimer(milliseconds: Long): String? {
            var finalTimerString = ""
            var secondsString = ""

            // Convert total duration into time
            val hours = (milliseconds / (1000 * 60 * 60)).toInt()
            val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
            val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
            // Add hours if there
            if (hours > 0) {
                finalTimerString = "$hours:"
            }

            // Prepending 0 to seconds if it is one digit
            secondsString = if (seconds < 10) {
                "0$seconds"
            } else {
                "" + seconds
            }
            finalTimerString = "$finalTimerString$minutes:$secondsString"

            // return timer string
            return finalTimerString
        }

        fun progressToTimer(progress: Int, totalDuration: Int): Int {
            var totalDuration = totalDuration
            var currentDuration = 0
            totalDuration = (totalDuration / 1000)
            currentDuration = (progress.toDouble() / 100 * totalDuration).toInt()

            // return current duration in milliseconds
            return currentDuration * 1000
        }


        fun getAlbumArtUri(albumId: Long): Uri? {
            return ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                albumId
            )
        }




    }






}



