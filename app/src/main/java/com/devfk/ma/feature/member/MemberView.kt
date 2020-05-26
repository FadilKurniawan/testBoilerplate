package com.devfk.ma.feature.member

import com.devfk.ma.base.presenter.MvpView
import com.devfk.ma.data.model.Guest
import io.realm.RealmResults

/**
 * Created by DODYDMW19 on 1/30/2018.
 */

interface MemberView : MvpView {

    fun onMemberCacheLoaded(members: RealmResults<Guest>?)

    fun onMemberLoaded(members: List<Guest>?)

    fun onMemberEmpty()

    fun onFailed(error: Any?)

}