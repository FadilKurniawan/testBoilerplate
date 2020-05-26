package com.devfk.ma.feature.member

import android.annotation.SuppressLint
import android.view.View
import com.devfk.ma.base.ui.adapter.viewholder.BaseItemViewHolder
import com.devfk.ma.data.model.Guest
import kotlinx.android.synthetic.main.item_member.view.*

/**
 * Created by DODYDMW19 on 1/30/2018.
 */
class MemberItemView(itemView: View) : BaseItemViewHolder<Guest>(itemView) {

    private var mActionListener: OnActionListener? = null
    private var guest: Guest? = null

    @SuppressLint("SetTextI18n")
    override fun bind(data: Guest?) {
        data.let {
            // for get context = itemView.context

            this.guest = data
            itemView.imgMember.setImageURI(data?.avatar)
            itemView.txtMemberName.text = data?.firstName + " " + data?.lastName
            itemView.button.setOnClickListener {
                mActionListener?.onClicked(this)
            }

        }
    }

    fun getData(): Guest {
        return guest!!
    }

    fun setOnActionListener(listener: OnActionListener) {
        mActionListener = listener
    }

    interface OnActionListener {
        fun onClicked(view: MemberItemView?)
    }
}