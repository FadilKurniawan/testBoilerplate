package com.devfk.ma.firebase.remoteconfig

import com.devfk.ma.base.presenter.MvpView

interface RemoteConfigView : MvpView {

    fun onUpdateAppNeeded(forceUpdate: Boolean, message: String?)

    fun onUpdateBaseUrlNeeded(type: String?, url: String?)

}