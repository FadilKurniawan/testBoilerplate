package com.devfk.ma.feature.menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.devfk.ma.R
import com.devfk.ma.base.ui.BaseActivity
import com.devfk.ma.data.local.RealmHelper
import com.devfk.ma.data.local.prefs.DataConstant
import com.devfk.ma.data.local.prefs.DataConstant.EVENT_RESULT_CODE_ACTIVITY
import com.devfk.ma.data.local.prefs.DataConstant.GUEST_RESULT_CODE_ACTIVITY
import com.devfk.ma.data.model.User
import com.devfk.ma.feature.event.MainEventActivity
import com.devfk.ma.feature.login.LoginActivity
import com.devfk.ma.feature.member.MemberActivity
import com.devfk.ma.firebase.analytics.FireBaseHelper
import com.devfk.ma.helper.socialauth.facebook.FacebookHelper
import com.devfk.ma.helper.socialauth.google.GoogleSignInHelper
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : BaseActivity(), MenuView{


    private var mFirebaseAnalytics: FireBaseHelper? = null
    private var mRealm: RealmHelper<User> = RealmHelper()
    private var mGoogleHelper: GoogleSignInHelper? = null
    private var mFbHelper: FacebookHelper? = null
    private var menuPresenter:MenuPresenter?= null


    override val resourceLayout: Int = R.layout.activity_menu

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupFirebase()
        setupPresenter()
        actionClick()
    }

    private fun setupFirebase() {
        mFirebaseAnalytics?.initialize(this)
    }

    private fun setupPresenter() {
        menuPresenter = MenuPresenter()
        menuPresenter?.attachView(this)
        menuPresenter?.getUser()
    }

    private fun actionClick() {
        btnEvent.setOnClickListener {
            goToActivity(EVENT_RESULT_CODE_ACTIVITY, MainEventActivity::class.java, null)
        }

        btnGuest.setOnClickListener {
            goToActivity(GUEST_RESULT_CODE_ACTIVITY, MemberActivity::class.java, null)
        }

        btnSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        mGoogleHelper?.performSignOut()
        mFbHelper?.performSignOut()
        mRealm.deleteData(User())
        goToActivity(LoginActivity::class.java, null, clearIntent = true, isFinish = true)
    }

    override fun OnUserLoaded(user: User) {
        tvName.text = user.name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == EVENT_RESULT_CODE_ACTIVITY){
                val eventNameResult = data!!.getStringExtra(DataConstant.EVENT_NAME_RESULT)
                val bundle = Bundle()
                bundle.putString("event_selected", eventNameResult)
                mFirebaseAnalytics?.sendEvent("event_name",bundle)
                btnEvent.text = eventNameResult
            }else if(requestCode == GUEST_RESULT_CODE_ACTIVITY){
                val guestNameResult = data!!.getStringExtra(DataConstant.GUEST_NAME_RESULT)
                val guestAgeResult = data.getStringExtra(DataConstant.GUEST_AGE_RESULT)
                val bundle = Bundle()
                bundle.putString("event_selected", guestNameResult)
                 mFirebaseAnalytics?.sendEvent("guest_name",bundle)
                btnGuest.text = guestNameResult
//                pop(guestAgeResult.toInt())
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
