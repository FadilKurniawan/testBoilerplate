package com.devfk.ma.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Event :RealmObject(){
    @PrimaryKey
    @SerializedName("id")
     var id:Int=0

    @SerializedName("title")
     var title:String=""

    @SerializedName("date")
     var date:String=""

    @SerializedName("image")
     var image:Int=0

    @SerializedName("hashtag")
     var hashtag: RealmList<String> = RealmList()

    @SerializedName("detail")
     var detail:String = ""

    @SerializedName("lattitude")
     var lattitude:String = ""

    @SerializedName("longtitude")
     var longtitude: String = ""
}