package com.devfk.ma.feature.event.eventMap

import android.os.Bundle
import com.devfk.ma.R
import com.devfk.ma.base.ui.BaseFragment
import com.devfk.ma.data.model.Event
import kotlinx.android.synthetic.main.item_event.*


class EventMapItemView : BaseFragment() {

    var data: Event? = null

    companion object {
        fun newInstance(data: Event): BaseFragment? {
            var fragment =  EventMapItemView()
            fragment.data = data
            return fragment
        }
    }

    override val resourceLayout: Int
        get() = R.layout.item_event

    override fun onViewReady(savedInstanceState: Bundle?) {
        imgEvent.setActualImageResource(data!!.image)
        tvNameEvent.text = data?.title

    }

}