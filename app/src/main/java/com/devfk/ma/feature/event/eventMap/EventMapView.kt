package com.devfk.ma.feature.event.eventMap

import com.devfk.ma.data.model.Event

interface EventMapView {
    fun onEventLoaded(list: List<Event>)
}