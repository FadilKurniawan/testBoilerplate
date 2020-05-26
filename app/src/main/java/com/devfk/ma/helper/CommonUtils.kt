package com.devfk.ma.helper

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import com.jakewharton.processphoenix.ProcessPhoenix
import com.devfk.ma.BuildConfig
import com.devfk.ma.data.local.RealmHelper
import com.devfk.ma.data.local.prefs.DataConstant
import com.devfk.ma.data.local.prefs.SuitPreferences
import com.devfk.ma.data.model.Guest
import com.devfk.ma.feature.splashscreen.SplashScreenActivity
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

/**
 * Created by dodydmw19 on 7/18/18.
 */

class CommonUtils {

    companion object {

        fun checkTwitterApp(context: Context): Boolean {
            return try {
                var info = context.packageManager.getApplicationInfo("com.twitter.android", 0)
                true
            } catch (e: NameNotFoundException) {
                false
            }
        }

        fun openAppInStore(context: Context) {
            // you can also use BuildConfig.APPLICATION_ID
            try {
                val appId = context.packageName
                val rateIntent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appId"))
                var marketFound = false
                // find all applications able to handle our rateIntent
                val otherApps = context.packageManager
                        .queryIntentActivities(rateIntent, 0)
                for (otherApp in otherApps) {
                    // look for Google Play application
                    if (otherApp.activityInfo.applicationInfo.packageName == "com.android.vending") {

                        val otherAppActivity = otherApp.activityInfo
                        val componentName = ComponentName(
                                otherAppActivity.applicationInfo.packageName,
                                otherAppActivity.name
                        )
                        // make sure it does NOT open in the stack of your activity
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        // task reparenting if needed
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                        // if the Google Play was already open in a search result
                        //  this make sure it still go to the app page you requested
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        // this make sure only the Google Play app is allowed to
                        // intercept the intent
                        rateIntent.component = componentName
                        context.startActivity(rateIntent)
                        marketFound = true
                        break

                    }
                }

                // if GP not present on device, open web browser
                if (!marketFound) {
                    val webIntent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appId"))
                    context.startActivity(webIntent)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun restartApp(activity: Activity?) {
            activity?.let {
                activity.run {
                    val intent = Intent(activity, SplashScreenActivity::class.java)
                    ProcessPhoenix.triggerRebirth(activity, intent)
                }
            }
        }

        fun restartApp(context: Context?) {
            context?.let {
                context.run {
                    val intent = Intent(context, SplashScreenActivity::class.java)
                    ProcessPhoenix.triggerRebirth(context, intent)
                }
            }
        }
        fun setCamera(lat: Double, lng: Double, mapBox: MapboxMap?) {
            val position = CameraPosition.Builder()
                    .target(LatLng(lat, lng))
                    .zoom(15.0)
                    .build()

            mapBox?.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), 3000)
        }

        fun clearLocalStorage() {
            val suitPreferences = SuitPreferences.instance()
            suitPreferences?.clearSession()
            val realm: RealmHelper<Guest> = RealmHelper()
            realm.removeAllData()
        }

        fun setDefaultBaseUrlIfNeeded() {
            val currentUrl: String? = SuitPreferences.instance()?.getString(DataConstant.BASE_URL)
            if (currentUrl == null) {
                SuitPreferences.instance()?.saveString(DataConstant.BASE_URL, BuildConfig.BASE_URL)
            }
        }

        fun createIntent(context: Context, actDestination: Class<out Activity>): Intent {
            return Intent(context, actDestination)
        }

    }

}