package com.devfk.ma.feature.event.eventList

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.devfk.ma.R
import com.devfk.ma.base.presenter.BasePresenter
import com.devfk.ma.data.local.RealmHelper
import com.devfk.ma.data.model.Event
import io.realm.RealmResults

class EventListPresenter(context: Context?) : BasePresenter<EventListView> {
    private var mContext: Context? = context
    private var mvpListView: EventListView? = null
    private var mRealm: RealmHelper<Event>? = RealmHelper()

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(listView: EventListView) {
        mvpListView = listView

        if(mvpListView is LifecycleOwner){
            (mvpListView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpListView = null
    }


    fun getEventCache() {

        var data: RealmResults<Event>? = mRealm?.getData(Event::class.java)
        if(data!!.isEmpty()) {
            val eventTitle = mContext?.resources!!.getStringArray(R.array.event_list_title)
            val eventDate = mContext?.resources!!.getStringArray(R.array.event_list_date)
            val eventLat = mContext?.resources!!.getStringArray(R.array.event_list_lattitude)
            val eventLong = mContext?.resources!!.getStringArray(R.array.event_list_longtitude)
            val eventHashtag = mContext?.resources!!.getStringArray(R.array.event_list_hashtag)

            for (eventCount in 0 until 4) {
                var eventItem: Event = Event().apply {
                    id = eventCount
                    title = eventTitle[eventCount]
                    date = eventDate[eventCount]
                    detail = mContext!!.resources.getString(R.string.txt_detail_event_item)
                    lattitude = eventLat[eventCount]
                    longtitude = eventLong[eventCount]
                }
                for (hashtagCount in 0 until 3) {
                    eventItem.hashtag.add(eventHashtag[hashtagCount])
                }
                eventItem.image = getDrawableFromResource(eventCount)
                mRealm?.saveObject(eventItem)
            }
            data = mRealm?.getData(Event::class.java)
        }
        mvpListView?.onEventCacheLoaded(data)
    }

    private fun getDrawableFromResource(eventCount: Int): Int {
        when(eventCount){
            0 -> return R.drawable.event1
            1 -> return R.drawable.event3
            2 -> return R.drawable.event2
            3 -> return R.drawable.event4
        }
        return 0
    }

    fun getEvent(currentPage: Int) {
        val data: RealmResults<Event>? = mRealm?.getData(Event::class.java)
        mvpListView?.onEventLoaded(data as List<Event>)
    }

}