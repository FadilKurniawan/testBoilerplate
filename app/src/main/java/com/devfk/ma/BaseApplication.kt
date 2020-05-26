package com.devfk.ma

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.onesignal.OneSignal
import com.devfk.ma.data.local.prefs.DataConstant
import com.devfk.ma.data.local.prefs.SuitPreferences
import com.devfk.ma.di.component.ApplicationComponent
import com.devfk.ma.di.component.DaggerApplicationComponent
import com.devfk.ma.di.module.ApplicationModule
import com.devfk.ma.helper.CommonConstant
import com.devfk.ma.helper.CommonUtils
import com.devfk.ma.onesignal.NotificationOpenedHandler
import com.mapbox.mapboxsdk.Mapbox
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class BaseApplication : MultiDexApplication() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        // Initial Preferences
        SuitPreferences.init(applicationContext)

        CommonUtils.setDefaultBaseUrlIfNeeded()

        appContext = applicationContext
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()

        Fresco.initialize(this)

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                //.encryptionKey(CommonUtils.getKey()) // encrypt realm
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(NotificationOpenedHandler())
                .init()
        OneSignal.enableVibrate(true)
        OneSignal.enableSound(true)
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.idsAvailable { userId, _ ->

            if (userId != null) {
                SuitPreferences.instance()?.saveString(DataConstant.PLAYER_ID, userId)
            }
        }
        Mapbox.getInstance(this, CommonConstant.MAP_BOX_TOKEN)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}