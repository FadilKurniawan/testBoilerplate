package com.devfk.ma.feature.event.eventMap

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devfk.ma.data.model.Event

class EventMapPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var listData: List<Event>? = null

    override fun getItem(p0: Int): Fragment {
        return listData?.get(p0)?.let { EventMapItemView.newInstance(it) }!!
    }

    override fun getCount(): Int {
        return listData?.size ?: 0
    }
}