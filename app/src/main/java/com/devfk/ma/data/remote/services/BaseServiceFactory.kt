package com.devfk.ma.data.remote.services

import android.os.Handler
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.devfk.ma.BaseApplication
import com.devfk.ma.BuildConfig
import com.devfk.ma.data.local.prefs.DataConstant
import com.devfk.ma.data.local.prefs.SuitPreferences
import com.devfk.ma.helper.CommonUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BaseServiceFactory {

    private var SERVICE_TAG = "apiServices"

    fun getAPIService(): APIService {
        return provideRetrofit(makeGSON())
    }

    private fun provideRetrofit(gSon: Gson): APIService {

        var url: String
        val currentUrl: String? = SuitPreferences.instance()?.getString(DataConstant.BASE_URL)

        if (currentUrl != null && currentUrl.isNotEmpty()) {
            currentUrl.let { url = currentUrl }
        } else {
            url = BuildConfig.BASE_URL
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(APIService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {

        val httpClientBuilder = OkHttpClient().newBuilder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
        }

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d(SERVICE_TAG, message) }
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }

        httpClientBuilder.addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            // check response un authorize user
            if (response.code() == 401) {
                CommonUtils.clearLocalStorage()
                Handler().postDelayed({
                    CommonUtils.restartApp(BaseApplication.appContext)
                }, 200)
            }

            response
        }

        return httpClientBuilder.build()
    }

    private fun makeGSON(): Gson {
        return GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

}