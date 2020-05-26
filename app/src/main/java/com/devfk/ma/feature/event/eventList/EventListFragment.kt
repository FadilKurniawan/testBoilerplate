package com.devfk.ma.feature.event.eventList

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.devfk.ma.R
import com.devfk.ma.base.ui.BaseFragment
import com.devfk.ma.base.ui.recyclerview.BaseRecyclerView
import com.devfk.ma.data.model.ErrorCodeHelper
import com.devfk.ma.data.model.Event
import com.devfk.ma.feature.event.MainEventActivity
import com.jcodecraeer.xrecyclerview.XRecyclerView
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_event_list.*
import kotlinx.android.synthetic.main.layout_base_shimmer.*

class EventListFragment : BaseFragment(),
        EventListView,
        EventItemView.OnActionListener,
        XRecyclerView.LoadingListener {


    companion object {
        fun newInstance(): BaseFragment? {
            return EventListFragment()
        }
    }

    private var currentPage: Int = 1
    private var eventListPresenter: EventListPresenter? = null
    private var eventListAdapter: EventListAdapter = EventListAdapter()

    override val resourceLayout: Int = R.layout.fragment_event_list

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupProgressView()
        setupEmptyView()
        setupErrorView()
        Handler().postDelayed({setupPresenter()},100)
    }


    private fun setupErrorView() {
        rvEvent.setImageErrorView(R.drawable.empty_state)
        rvEvent.setTitleErrorView(getString(R.string.txt_error_no_internet))
        rvEvent.setContentErrorView(getString(R.string.txt_error_connection))
        rvEvent.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {
            override fun onClick(v: View?) {
                currentPage = 1
                loadData()
            }

        })
    }

    private fun setupEmptyView() {
        rvEvent.setImageEmptyView(R.drawable.empty_state)
        rvEvent.setTitleEmptyView(getString(R.string.txt_empty_event))
        rvEvent.setContentEmptyView(getString(R.string.txt_empty_event_content))
        rvEvent.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener{
            override fun onClick(v: View?) {
                currentPage = 1
                loadData()
            }

        })
    }


    private fun setupProgressView() {
        R.layout.layout_shimmer_event.apply {
            viewStub.layoutResource = this
        }
        viewStub.inflate()
    }

    private fun setupRecyclerView() {
        eventListAdapter.setOnActionListener(this)

        rvEvent.apply {
            setUpAsList()
            setAdapter(eventListAdapter)
            setPullToRefreshEnable(true)
            setLoadingMoreEnabled(false)
            setLoadingListener(object: XRecyclerView.LoadingListener{
                override fun onLoadMore() {
                    currentPage++
                    loadData()
                }

                override fun onRefresh() {
                    currentPage = 1
                    loadData()
                }

            })
        }
    }

    private fun loadData() {
        eventListPresenter?.getEvent(currentPage)
    }


    private fun setupPresenter() {
        eventListPresenter = EventListPresenter(context)
        eventListPresenter?.attachView(this)
        eventListPresenter?.getEventCache()
        rvEvent.initialShimmer()
        eventListPresenter?.getEvent(currentPage)
    }

    private fun setData(data: List<Event>?) {
        data?.let {
            if (currentPage == 1) {
                eventListAdapter.clear()
            }
            eventListAdapter.add(it)
        }
        rvEvent.stopShimmer()
        rvEvent.showRecycler()
        rvEvent.completeRefresh()
    }
    override fun onEventCacheLoaded(event: RealmResults<Event>?) {
        event.let {
            if (event?.isNotEmpty()!!) {
                setData(event)
            }
        }
        finishLoad(rvEvent)
        println("*** should be loaded")
    }

    override fun onEventLoaded(event: List<Event>) {
        event.let {
            if (event?.isNotEmpty()!!) {
                setData(event)
            }
        }
        finishLoad(rvEvent)
    }

    override fun onEventEmpty() {
        finishLoad(rvEvent)
        rvEvent.showEmpty()
    }

    override fun onFailed(error: Any?) {
        finishLoad(rvEvent)
        rvEvent.showEmpty()
        error?.let { ErrorCodeHelper.getErrorMessage(context, it)?.let { msg -> showToast(msg) } }
    }

    override fun onClicked(view: EventItemView?) {
        view?.getData()?.let {
            (activity as MainEventActivity).onItemClick(view.getData().title)
        }
    }

    override fun onLoadMore() {
        currentPage++
        loadData()
    }

    override fun onRefresh() {
        currentPage = 1
        loadData()
    }

}
