package com.devfk.ma.feature.splashscreen

import android.os.Bundle
import com.devfk.ma.R
import com.devfk.ma.base.ui.BaseActivity
import com.devfk.ma.feature.login.LoginActivity
import com.devfk.ma.helper.CommonConstant
import kotlinx.android.synthetic.main.activity_splashscreen.*

/**
 * Created by dodydmw19 on 12/19/18.
 */

class SplashScreenActivity : BaseActivity(), SplashScreenView {

    private var splashScreenPresenter: SplashScreenPresenter? = null
    private val actionClicked = ::dialogPositiveAction

    override val resourceLayout: Int = R.layout.activity_splashscreen

    override fun onViewReady(savedInstanceState: Bundle?) {
        changeProgressBarColor(R.color.white, progressBar)
        setupPresenter()
    }

    private fun handleIntent(){
        val data: Bundle? = intent.extras
        if(data?.getString(CommonConstant.APP_CRASH) != null){
            showConfirmationSingleDialog(getString(R.string.txt_error_crash), actionClicked)
        }else{
            splashScreenPresenter?.initialize()
        }
    }

    private fun setupPresenter() {
        splashScreenPresenter = SplashScreenPresenter()
        splashScreenPresenter?.attachView(this)
        handleIntent()
    }

    override fun navigateToMainView() {
        goToActivity(LoginActivity::class.java,  null, clearIntent = true, isFinish = true)
    }

    private fun dialogPositiveAction() {
        splashScreenPresenter?.initialize()
    }

}