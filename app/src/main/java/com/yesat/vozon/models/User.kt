package com.yesat.vozon.models

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import java.io.Serializable
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


class User : Serializable {

    @Expose var id: Long? = null
    @Expose var phone: String? = null
    @Expose var name: String? = null
    @Expose var about: String? = null
    @Expose var type: String? = null
    @Expose var city: Location? = null
    @Expose var avatar: String? = null
    @Expose var experience: Long? = null
    @Expose var courier_type: Long? = null
    var courierTypeName: String?
        get() = when (courier_type?.toInt()){
                1 -> "Физическое лицо"
                2 -> "Юридическое лицо"
                else -> null
            }
        set(value) = when (value){
            "Физическое лицо" -> courier_type = 1
            "Юридическое лицо" -> courier_type = 2
            else -> {}
        }

    @Expose var rating: String? = null
        get() = if (field?.trim() != "-1") field else ""
    @Expose var token: String? = null

    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    override fun toString(): String {
        return """
            |User(id=$id,
            |   phone=$phone,
            |   name=$name,
            |   about=$about,
            |   type=$type,
            |   avatar=$avatar,
            |   experience=$experience,
            |   courier_type=$courier_type,
            |   rating=$rating,
            |   token=$token
            |)""".trimMargin()
    }

    fun callIntent(): Intent{
        return Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
    }


    companion object {
        const val CLIENT = "client"
        const val COURIER = "courier"

        fun fromJson(json: String): User {
            return Gson().fromJson(json, User::class.java)
        }
    }

}