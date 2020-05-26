package com.devfk.ma.feature.menu

import com.devfk.ma.base.presenter.BasePresenter
import com.devfk.ma.data.local.RealmHelper
import com.devfk.ma.data.model.User
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults

class MenuPresenter : BasePresenter<MenuView>{

    private var mvpView: MenuView? = null
    private var mRealm: RealmHelper<User>? = RealmHelper()
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    fun getUser(){
        val data = mRealm?.getSingleData(User())
        if (data != null) {
            mvpView?.OnUserLoaded(data)
        }
    }

    override fun onDestroy() {

    }

    override fun attachView(view: MenuView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
        mCompositeDisposable.let { mCompositeDisposable?.clear() }
    }

}