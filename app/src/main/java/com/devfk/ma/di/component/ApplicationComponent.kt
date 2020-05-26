package com.devfk.ma.di.component

import com.devfk.ma.di.module.ApplicationModule
import com.devfk.ma.di.scope.SuitCoreApplicationScope
import com.devfk.ma.feature.event.eventList.EventListPresenter
import com.devfk.ma.feature.event.eventMap.EventMapPresenter
import com.devfk.ma.feature.login.LoginPresenter
import com.devfk.ma.feature.member.MemberPresenter
import com.devfk.ma.feature.menu.MenuPresenter
import com.devfk.ma.feature.splashscreen.SplashScreenPresenter
import com.devfk.ma.firebase.remoteconfig.RemoteConfigPresenter
import com.devfk.ma.onesignal.OneSignalPresenter
import dagger.Component

@SuitCoreApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(eventMapPresenter: EventMapPresenter)

    fun inject(eventListPresenter: EventListPresenter)

    fun inject(menuPresenter: MenuPresenter)

    fun inject(memberPresenter: MemberPresenter)

    fun inject(loginPresenter: LoginPresenter)

    fun inject(splashScreenPresenter: SplashScreenPresenter)

    fun inject(oneSignalPresenter: OneSignalPresenter)

    fun inject(remoteConfigPresenter: RemoteConfigPresenter)
}