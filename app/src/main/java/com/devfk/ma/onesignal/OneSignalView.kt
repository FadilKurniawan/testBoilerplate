package com.devfk.ma.onesignal

import com.devfk.ma.base.presenter.MvpView

/**
 * Created by dodydmw19 on 6/12/19.
 */

interface OneSignalView : MvpView {

    fun onRegisterIdSuccess(message: String?)

    fun onRegisterIdFailed(error: Any?)

}