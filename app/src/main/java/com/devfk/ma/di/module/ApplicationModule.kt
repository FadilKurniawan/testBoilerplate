package com.devfk.ma.di.module

import android.app.Application
import android.content.Context
import com.devfk.ma.data.remote.services.APIService
import com.devfk.ma.data.remote.services.BaseServiceFactory
import com.devfk.ma.di.scope.SuitCoreApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    @Provides
    @SuitCoreApplicationScope
    internal fun provideAPIService(): APIService {
        return BaseServiceFactory.getAPIService()
    }
}
