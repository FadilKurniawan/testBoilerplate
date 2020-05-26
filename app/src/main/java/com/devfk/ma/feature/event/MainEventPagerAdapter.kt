package com.devfk.ma.feature.event


import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devfk.ma.base.ui.BaseFragment
import com.devfk.ma.feature.event.eventList.EventListFragment
import com.devfk.ma.feature.event.eventMap.EventMapFragment

class MainEventPagerAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){


    private var eventListFragment: BaseFragment? = null
    private var eventMapFragment: BaseFragment? = null

    override fun getItem(position: Int): BaseFragment = when(position) {
        0 -> generateEventListFragment()
        1 -> generateEventMapFragment()
        else -> generateEventListFragment()
    }

    override fun getCount(): Int = 2

    private fun generateEventListFragment(): BaseFragment = if (eventListFragment == null) {
        EventListFragment.newInstance()!!
    }else{
        eventListFragment!!
    }

    private fun generateEventMapFragment(): BaseFragment = if (eventMapFragment == null) {
        EventMapFragment.newInstance()!!
    }else{
        eventMapFragment!!
    }
}
