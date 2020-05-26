package com.devfk.ma.data.model.deserelializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.devfk.ma.data.model.Guest
import java.lang.reflect.Type

/**
 * Created by DODYDMW19 on 1/30/2018.
 */
class UserDeserializer : SuitCoreJsonDeserializer<Guest>() {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Guest {
        val jsonObject = json?.asJsonObject

        val guest: Guest? = Guest()
        if (jsonObject?.has("id")!!) {
            guest?.id = getIntOrZeroFromJson(jsonObject.get("id"))
        }

        if (jsonObject.has("first_name")) {
            guest?.firstName = getStringOrNullFromJson(jsonObject.get("first_name"))
        }

        if (jsonObject.has("last_name")) {
            guest?.lastName = getStringOrNullFromJson(jsonObject.get("last_name"))
        }

        if (jsonObject.has("avatar")) {
            guest?.avatar = getStringOrNullFromJson(jsonObject.get("avatar"))
        }

        return guest!!

    }
}