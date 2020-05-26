package com.devfk.ma.feature.menu

import com.devfk.ma.base.presenter.MvpView
import com.devfk.ma.data.model.User

interface MenuView :MvpView{
    fun OnUserLoaded(user: User)
}