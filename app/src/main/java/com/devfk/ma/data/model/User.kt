package com.devfk.ma.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User (
    @PrimaryKey
    var id: Int? = 0,
    var name:String=""
):RealmObject()

