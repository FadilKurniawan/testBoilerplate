package com.devfk.ma.feature.member

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.devfk.ma.R
import com.devfk.ma.base.ui.BaseActivity
import com.devfk.ma.base.ui.recyclerview.BaseRecyclerView
import com.devfk.ma.data.model.ErrorCodeHelper
import com.devfk.ma.data.model.Guest
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.devfk.ma.feature.fragmentsample.SampleActivity
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_member.*
import kotlinx.android.synthetic.main.layout_base_shimmer.*

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

class MemberActivity : BaseActivity(),
        MemberView,
        MemberItemView.OnActionListener,
        XRecyclerView.LoadingListener {

    private var memberPresenter: MemberPresenter? = null

    private var currentPage: Int = 1

    private var memberAdapter: MemberAdapter = MemberAdapter()

    override val resourceLayout: Int = R.layout.activity_member

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupProgressView()
        setupEmptyView()
        setupErrorView()
        actionClick()
        Handler().postDelayed({ setupPresenter() }, 100)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearRecyclerView(rvMember)
    }

    private fun setupPresenter() {
        memberPresenter = MemberPresenter()
        memberPresenter?.attachView(this)
        memberPresenter?.getMemberCache()
        rvMember.initialShimmer()
        memberPresenter?.getMember(currentPage)
    }

    private fun setupRecyclerView() {
        memberAdapter.setOnActionListener(this)

        rvMember.apply {
            setUpAsList()
            setAdapter(memberAdapter)
            setPullToRefreshEnable(true)
            setLoadingMoreEnabled(true)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {
                    currentPage = 1
                    loadData()
                }

                override fun onLoadMore() {
                    currentPage++
                    loadData()
                }
            })
        }
        rvMember.setLayoutManager(GridLayoutManager(this,2))

    }

    private fun loadData() {
        memberPresenter?.getMember(currentPage)
    }

    private fun setData(data: List<Guest>?) {
        data?.let {
            if (currentPage == 1) {
                memberAdapter.clear()
            }
            memberAdapter.add(it)
        }
        rvMember.stopShimmer()
        rvMember.showRecycler()
        rvMember.completeRefresh()
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_member.apply {
            viewStub.layoutResource = this
        }

        viewStub.inflate()
    }

    private fun setupEmptyView() {
        rvMember.setImageEmptyView(R.drawable.empty_state)
        rvMember.setTitleEmptyView(getString(R.string.txt_empty_member))
        rvMember.setContentEmptyView(getString(R.string.txt_empty_member_content))
        rvMember.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                currentPage = 1
                loadData()
            }

        })
    }

    private fun setupErrorView() {
        rvMember.setImageErrorView(R.drawable.empty_state)
        rvMember.setTitleErrorView(getString(R.string.txt_error_no_internet))
        rvMember.setContentErrorView(getString(R.string.txt_error_connection))
        rvMember.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                currentPage = 1
                loadData()
            }

        })
    }

    override fun onMemberCacheLoaded(members: RealmResults<Guest>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(rvMember)
    }

    override fun onMemberLoaded(members: List<Guest>?) {
        members.let {
            if (members?.isNotEmpty()!!) {
                setData(members)
            }
        }
        finishLoad(rvMember)
    }

    override fun onLoadMore() {
        currentPage++
        loadData()
    }

    override fun onRefresh() {
        currentPage = 1
        loadData()
    }

    override fun onMemberEmpty() {
        finishLoad(rvMember)
        rvMember.showEmpty()
    }

    override fun onFailed(error: Any?) {
        finishLoad(rvMember)
        rvMember.showEmpty()
        error?.let { ErrorCodeHelper.getErrorMessage(this, it)?.let { msg -> showToast(msg) } }
    }

    override fun onClicked(view: MemberItemView?) {
        view?.getData()?.let {
            val intent = Intent().apply {
                putExtra("guestResultName",view?.getData().firstName+" "+view?.getData().lastName)
            }
            setResult(Activity.RESULT_OK,intent)
            super.onBackPressed()
        }
    }

    private fun actionClick() {

    }
}