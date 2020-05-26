package com.devfk.ma.feature.login

import com.devfk.ma.BaseApplication
import com.devfk.ma.base.presenter.BasePresenter
import com.devfk.ma.data.local.RealmHelper
import com.devfk.ma.data.model.User

/**
 * Created by dodydmw19 on 7/18/18.
 */

class LoginPresenter : BasePresenter<LoginView>{

    private var mvpView: LoginView? = null
    private var mRealm:RealmHelper<User>? = RealmHelper()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun login(name: String?) {
        name?.let { User(1,it) }?.let { mRealm?.saveObject(it) }
        mvpView?.onLoginSuccess("success")
    }

    fun checkUserExist(){
        if(mRealm?.getSingleData(User())!=null){
            mvpView?.onLoginSuccess("success")
        }
    }

    override fun onDestroy() {
    }

    override fun attachView(view: LoginView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}