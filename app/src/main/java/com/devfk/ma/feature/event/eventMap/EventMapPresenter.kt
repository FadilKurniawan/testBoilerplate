package com.devfk.ma.feature.event.eventMap

import com.devfk.ma.BaseApplication
import com.devfk.ma.base.presenter.BasePresenter
import com.devfk.ma.data.local.RealmHelper
import com.devfk.ma.data.model.Event
import io.realm.RealmResults

class EventMapPresenter: BasePresenter<EventMapView>{

    private var mvpView: EventMapView? = null

    private var mRealm: RealmHelper<Event>? = RealmHelper()
    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun getEvent() {
        val data: RealmResults<Event>? = mRealm?.getData(Event::class.java)
        mvpView?.onEventLoaded(data as List<Event>)
    }

    override fun onDestroy() {}

    override fun attachView(view: EventMapView) {
        mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}