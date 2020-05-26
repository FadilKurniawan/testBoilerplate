package com.devfk.ma.feature.member

import android.view.ViewGroup
import com.devfk.ma.R
import com.devfk.ma.base.ui.adapter.BaseRecyclerAdapter
import com.devfk.ma.data.model.Guest

/**
 * Created by DODYDMW19 on 1/30/2018.
 */
class MemberAdapter : BaseRecyclerAdapter<Guest, MemberItemView>() {

    private var mOnActionListener: MemberItemView.OnActionListener? = null

    fun setOnActionListener(onActionListener: MemberItemView.OnActionListener) {
        mOnActionListener = onActionListener
    }

    override fun getItemResourceLayout(): Int  = R.layout.item_member

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MemberItemView{
        val view = MemberItemView(getView(parent))
        mOnActionListener?.let { view.setOnActionListener(it) }
        return view
    }

}