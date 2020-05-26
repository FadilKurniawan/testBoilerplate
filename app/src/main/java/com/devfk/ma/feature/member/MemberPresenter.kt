package com.devfk.ma.feature.member

import androidx.lifecycle.LifecycleOwner
import com.devfk.ma.BaseApplication
import com.devfk.ma.R
import com.devfk.ma.base.presenter.BasePresenter
import com.devfk.ma.data.local.RealmHelper
import com.devfk.ma.data.model.Guest
import com.devfk.ma.data.remote.services.APIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import javax.inject.Inject

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class MemberPresenter : BasePresenter<MemberView> {

    @Inject
    lateinit var apiService: APIService
    private var mvpView: MemberView? = null
    private var mRealm: RealmHelper<Guest>? = RealmHelper()
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun getMemberCache() {
        /* from Realm Model */
        val data: RealmResults<Guest>? = mRealm?.getData(Guest::class.java)

        //if (data == null) data = emptyList()

        mvpView?.onMemberCacheLoaded(data)
    }

    fun getMember(currentPage: Int?) {
        mCompositeDisposable?.add(
                apiService.getMembers(10, currentPage!!)
                        .map {
                            saveToCache(it.arrayData, currentPage)
                            it
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ data ->
                            if (data != null) {
                                if (currentPage == 1) {
                                    if (data.arrayData?.isNotEmpty()!!) {
                                        mvpView?.onMemberLoaded(data.arrayData!!)
                                    } else {
                                        mvpView?.onMemberEmpty()
                                    }
                                } else {
                                    mvpView?.onMemberLoaded(data.arrayData!!)
                                }
                            } else {
                                mvpView?.onFailed(R.string.txt_error_global)
                            }
                        }, {
                            mvpView?.onFailed(it)
                        })
        )
    }

    private fun saveToCache(data: List<Guest>?, currentPage: Int?) {
        if (data != null && data.isNotEmpty()) {
            if (currentPage == 1) {
                // remove current realm data
                mRealm?.deleteData(Guest())
            }

            // save to realm
            mRealm?.saveList(data)
        }
    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: MemberView) {
        mvpView = view
        // Initialize this presenter as a lifecycle-aware when a view is a lifecycle owner.
        if (mvpView is LifecycleOwner) {
            (mvpView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpView = null
        mCompositeDisposable.let { mCompositeDisposable?.clear() }
    }
}