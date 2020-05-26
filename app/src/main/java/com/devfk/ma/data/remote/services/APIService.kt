package com.devfk.ma.data.remote.services

import com.devfk.ma.data.model.Guest
import com.devfk.ma.data.remote.wrapper.Results
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by DODYDMW19 on 8/3/2017.
 */

interface APIService {

    @GET("users")
    fun getMembers(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int): Single<Results<Guest>>

}
