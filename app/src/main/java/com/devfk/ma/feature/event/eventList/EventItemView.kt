package com.devfk.ma.feature.event.eventList

import android.view.View
import com.devfk.ma.base.ui.adapter.viewholder.BaseItemViewHolder
import com.devfk.ma.data.model.Event
import kotlinx.android.synthetic.main.item_event.view.*

class EventItemView(itemView: View): BaseItemViewHolder<Event>(itemView) {

    private var mActionListener: OnActionListener? = null
    private var event: Event? = null

    override fun bind(data: Event?) {
        data.let {
            // for get context = itemView.context
            this.event = data
            itemView.imgEvent.setActualImageResource(data!!.image)
            itemView.tvNameEvent.text = data?.title
            itemView.button.setOnClickListener {
                mActionListener?.onClicked(this)
            }

        }
    }
    fun getData(): Event {
        return event!!
    }

    fun setOnActionListener(listener: OnActionListener) {
        mActionListener = listener
    }


    interface OnActionListener {
        fun onClicked(view: EventItemView?)
    }
}