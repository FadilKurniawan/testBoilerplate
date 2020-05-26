package com.devfk.ma.feature.login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.devfk.ma.R
import com.devfk.ma.base.ui.BaseActivity
import com.devfk.ma.feature.member.MemberActivity
import com.devfk.ma.feature.menu.MenuActivity
import com.devfk.ma.firebase.remoteconfig.RemoteConfigHelper
import com.devfk.ma.firebase.remoteconfig.RemoteConfigPresenter
import com.devfk.ma.firebase.remoteconfig.RemoteConfigView
import com.devfk.ma.helper.CommonConstant
import com.devfk.ma.helper.CommonUtils
import com.devfk.ma.helper.permission.SuitPermissions
import com.devfk.ma.helper.socialauth.facebook.FacebookHelper
import com.devfk.ma.helper.socialauth.facebook.FacebookListener
import com.devfk.ma.helper.socialauth.google.GoogleListener
import com.devfk.ma.helper.socialauth.google.GoogleSignInHelper
import com.devfk.ma.helper.socialauth.twitter.TwitterHelper
import com.devfk.ma.helper.socialauth.twitter.TwitterListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.form_login.*

/**
 * Created by dodydmw19 on 7/18/18.
 */

class LoginActivity : BaseActivity(), LoginView, RemoteConfigView, GoogleListener, FacebookListener, TwitterListener {

    private var loginPresenter: LoginPresenter? = null
    private var remoteConfigPresenter: RemoteConfigPresenter? = null

    private var mGoogleHelper: GoogleSignInHelper? = null
    private var mTwitterHelper: TwitterHelper? = null
    private var mFbHelper: FacebookHelper? = null

    override val resourceLayout: Int = R.layout.activity_login


    override fun onViewReady(savedInstanceState: Bundle?) {
        setupPresenter()
        setupSocialLogin()
        actionClicked()
        needPermissions()
    }

    override fun onResume() {
        super.onResume()
        remoteConfigPresenter?.checkUpdate(CommonConstant.CHECK_APP_VERSION) // check app version and notify update from remote config
        remoteConfigPresenter?.checkUpdate(CommonConstant.CHECK_BASE_URL) // check base url from remote config if any changes
    }

    private fun setupPresenter() {
        loginPresenter = LoginPresenter()
        loginPresenter?.attachView(this)
        loginPresenter?.checkUserExist()
    }

    private fun needPermissions() {
        SuitPermissions.with(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onAccepted {
                    for (s in it) {
                        Log.d("granted_permission", s)
                    }
                    showToast("Granted")
                }
                .onDenied {
                    showToast("Denied")
                }
                .onForeverDenied {
                    showToast("Forever denied")
                }
                .ask()
    }

    private fun setupSocialLogin() {
        // Google  initialization
        mGoogleHelper = GoogleSignInHelper(this, R.string.default_web_client_id, this)

        // twitter initialization
        mTwitterHelper = TwitterHelper(
                R.string.twitter_api_key,
                R.string.twitter_secret_key,
                this,
                this)

        // fb initialization
        mFbHelper = FacebookHelper(this, getString(R.string.facebook_request_field))

        signOut()
    }

    private fun signOut() {
        mGoogleHelper?.performSignOut()
        mFbHelper?.performSignOut()
    }

    override fun onLoginSuccess(message: String?) {
        goToActivity(MenuActivity::class.java, null, clearIntent = true, isFinish = true)
    }

    override fun onLoginFailed(message: String?) {
        message?.let {
            showToast(message.toString())
        }
    }

    override fun onGoogleAuthSignIn(authToken: String?, userId: String?) {
        // send token & user_id to server
//        println("*** userId : $userId")
//        println("*** token : $authToken")
        loginPresenter?.login(userId)
    }

    override fun onGoogleAuthSignInFailed(errorMessage: String?) {
        showToast(errorMessage.toString())
    }

    override fun onFbSignInFail(errorMessage: String?) {
        showToast(errorMessage.toString())
    }

    override fun onFbSignInSuccess(authToken: String?, userId: String?) {
        // send token & user_id to server
        loginPresenter?.login(userId)
    }

    override fun onTwitterError(errorMessage: String?) {
        showToast(errorMessage.toString())
    }

    override fun onTwitterSignIn(authToken: String?, secret: String?, userId: String?) {
        // send token & user_id to server
        loginPresenter?.login(userId)
    }

    override fun onUpdateAppNeeded(forceUpdate: Boolean, message: String?) {
        when (forceUpdate) {
            true -> {
                val confirmDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(message)
                        .setPositiveButton("OK") { d, _ ->
                            d.dismiss()
                            CommonUtils.openAppInStore(this)
                        }
                        .create()

                confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                confirmDialog.show()
            }
            false -> {
                val confirmDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(message)
                        .setPositiveButton("OK") { d, _ ->
                            d.dismiss()
                            CommonUtils.openAppInStore(this)
                        }
                        .setNegativeButton("CANCEL") { d, _ -> d.dismiss() }
                        .create()

                confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                confirmDialog.show()
            }
        }
    }

    override fun onUpdateBaseUrlNeeded(type: String?, url: String?) {
        RemoteConfigHelper.changeBaseUrl(this, type.toString(), url.toString())
    }

    private fun actionClicked() {

        btnNext.setOnClickListener{
            loginPresenter?.login(etName.text.toString())
        }

        btnCheck.setOnClickListener{
            val confirmDialog = AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(
                            OnCheckPalindrome(etPalindrome.text.toString())
                    )
                    .setPositiveButton("OK") { d, _ ->
                        d.dismiss()
                    }
                    .create()

            confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            confirmDialog.show()
        }
        relGoogle.setOnClickListener {
            mGoogleHelper?.performSignIn(this)
        }

        relFacebook.setOnClickListener {
            mFbHelper?.performSignIn(this)
        }

        relTwitter.setOnClickListener {
            if (CommonUtils.checkTwitterApp(this)) {
                mTwitterHelper?.performSignIn()
            } else {
                showToast(getString(R.string.txt_twitter_not_installed))
            }
        }

        tvSkip.setOnClickListener {
            goToActivity(MemberActivity::class.java, null, clearIntent = true, isFinish = true)
        }
    }

    private fun OnCheckPalindrome(sentence: String): String {
        var string = sentence.replace(" ","")
        var reverse = string.reversed()
        if(string.equals(reverse)){
            return "is Polindrome"
        }else{
            return "not Polindrome"
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            mGoogleHelper?.onActivityResult(requestCode, resultCode, data)
            mTwitterHelper?.onActivityResult(requestCode, resultCode, data)
            mFbHelper?.onActivityResult(requestCode, resultCode, data)
        }
    }

}