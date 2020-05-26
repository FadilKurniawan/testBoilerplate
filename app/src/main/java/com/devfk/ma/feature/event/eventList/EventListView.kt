package com.devfk.ma.feature.event.eventList

import com.devfk.ma.base.presenter.MvpView
import com.devfk.ma.data.model.Event
import io.realm.RealmResults

interface EventListView:MvpView {

    fun onEventCacheLoaded(event: RealmResults<Event>?)

    fun onEventLoaded(event: List<Event>)

    fun onEventEmpty()

    fun onFailed(error: Any?)

}