package com.devfk.ma.feature.event.eventMap

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import androidx.viewpager.widget.ViewPager

import com.devfk.ma.R
import com.devfk.ma.base.ui.BaseFragment
import com.devfk.ma.data.model.Event
import com.devfk.ma.feature.event.eventList.EventListPresenter
import com.devfk.ma.feature.event.eventList.EventListView
import com.devfk.ma.helper.CommonUtils
import com.google.gson.JsonPrimitive
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_event_map.*


class EventMapFragment : BaseFragment(), EventMapView {

    private var mapBox: MapboxMap? = null
    private var eventListPresenter: EventMapPresenter? = null
    private var symbolManager: SymbolManager? = null
    private var arrayEvent = emptyList<Event>()
    private var adapter: EventMapPagerAdapter? = null
    private var hashMapEvents: HashMap<Int, Int> = hashMapOf()

    private var markerNormal = "marker_normal"
    private var markerSelected = "marker_selected"

    override val resourceLayout: Int =  R.layout.fragment_event_map

    companion object {
        fun newInstance(): BaseFragment? {
            return EventMapFragment()
        }
    }


    override fun onViewReady(savedInstanceState: Bundle?) {
        Handler().postDelayed({setupPresenter()},100)
    }

    private fun setupPresenter() {
        eventListPresenter = EventMapPresenter()
        eventListPresenter?.attachView(this)
        eventListPresenter?.getEvent()
    }




    override fun onEventLoaded(event: List<Event>) {
        arrayEvent = event!!
        setupMap(event)

    }

    private fun setupMap(events: List<Event>) {
        setupPager(events)
        mapEvent.getMapAsync { mapBoxMap ->
            this.mapBox = mapBoxMap

            mapBoxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                style.addImage(markerNormal, BitmapFactory.decodeResource(this.resources, R.drawable.ic_marker_unselected))
                style.addImage(markerSelected, BitmapFactory.decodeResource(this.resources, R.drawable.ic_marker_selected))

                // create symbol manager
                val geoJsonOptions = GeoJsonOptions().withTolerance(0.4f)
                symbolManager = SymbolManager(mapEvent, mapBoxMap, style, null, geoJsonOptions)
                symbolManager?.addClickListener { symbol ->
                    showToast("value : " + symbol.data?.asString.toString())
                    val pos = hashMapEvents.filterKeys { it == symbol.data?.asInt }.getValue(symbol.data?.asInt!!)
                    setSelectedMarker(pos, symbolManager)
                }
                events.forEachIndexed { index, event ->
                    symbolManager?.create(createCustomMarker(event.lattitude.toDouble(), event.longtitude.toDouble(), event, index))
                }

                events.first().let { event ->
                    CommonUtils.setCamera(event.lattitude.toDouble(), event.longtitude.toDouble(), mapBox)
                }
            }
        }
    }

    private fun createCustomMarker(latitude: Double, longitude: Double, event: Event, index: Int): SymbolOptions {
        return SymbolOptions()
                .withData(JsonPrimitive(event.id.toString()))
                .withLatLng(LatLng(latitude, longitude))
                .withIconImage(if (index == 0) markerSelected else markerNormal)
                .withIconSize(1.0f)
                .withSymbolSortKey(10.0f)
                .withDraggable(false)
    }

    private fun setupPager(events: List<Event>) {
        adapter = EventMapPagerAdapter(childFragmentManager)

        events.forEachIndexed { index, event ->
            hashMapEvents[event.id!!] = index
        }

        adapter?.listData = events
        vpEvent.adapter = adapter

        vpEvent.clipToPadding = false
        vpEvent.setPadding(50, 0, 50, 0)
        vpEvent.pageMargin = 20

        vpEvent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                setSelectedMarker(p0, symbolManager)
            }
        })
    }


    private fun setSelectedMarker(position: Int, symbolManager: SymbolManager?) {

        val symbol = symbolManager?.annotations?.valueAt(position)
        symbol?.iconImage = markerSelected
        symbolManager?.symbolPlacement = markerSelected
        symbolManager?.update(symbol)

        vpEvent.currentItem = position
        for (item in 0 until symbolManager?.annotations?.size()!!) {
            val currentSymbol = symbolManager.annotations!![item.toLong()]
            if (currentSymbol?.id != symbol?.id) {
                currentSymbol?.iconImage = markerNormal
                symbolManager.symbolPlacement = markerNormal
                symbolManager.update(currentSymbol)
            }
        }

        symbol.let { it?.latLng?.longitude?.let { it1 -> CommonUtils.setCamera(it.latLng.latitude, it1, mapBox) } }
    }



}
