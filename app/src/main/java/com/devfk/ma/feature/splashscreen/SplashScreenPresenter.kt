package com.devfk.ma.feature.splashscreen

import android.os.Handler
import com.devfk.ma.BaseApplication
import com.devfk.ma.base.presenter.BasePresenter

/**
 * Created by dodydmw19 on 12/19/18.
 */

class SplashScreenPresenter : BasePresenter<SplashScreenView> {

    private var mvpView: SplashScreenView? = null
    private val time: Long = 3000

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    override fun onDestroy() {
        detachView()
    }

    fun initialize() {
        Handler().postDelayed({ mvpView?.navigateToMainView() }, time)
    }

    override fun attachView(view: SplashScreenView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}