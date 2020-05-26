package com.devfk.ma.feature.event.eventList

import android.view.ViewGroup
import com.devfk.ma.R
import com.devfk.ma.base.ui.adapter.BaseRecyclerAdapter
import com.devfk.ma.data.model.Event

class EventListAdapter : BaseRecyclerAdapter<Event, EventItemView>(){

    private var mOnActionListener: EventItemView.OnActionListener? = null

    fun setOnActionListener(onActionListener: EventItemView.OnActionListener){
        mOnActionListener = onActionListener
    }

    override fun getItemResourceLayout(): Int = R.layout.item_event

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemView {
       val view = EventItemView(getView(parent))
        mOnActionListener?.let{view.setOnActionListener(it)}
        return view
    }

}