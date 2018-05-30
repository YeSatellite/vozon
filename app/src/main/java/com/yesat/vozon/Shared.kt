package com.yesat.vozon

import android.app.Activity
import android.content.SharedPreferences
import android.nfc.Tag
import android.util.Log
import com.yesat.vozon.models.InfoTmp
import com.yesat.vozon.models.User
import retrofit2.Call

object Shared {
    object Action {
        const val acceptOffer = "accept_offer"
        const val done = "done"
        const val newOrder= "new_order"
    }

    const val user = "user"
    const val action = "action"

    const val posted = "posted"
    const val waiting = "waiting"
    const val active = "active"

    var call: Call<List<InfoTmp>>? = null

    var preferences: SharedPreferences? = null
        private set
    var currentUser = User()
        set(value) {
            field = value
            val editor = preferences!!.edit()
            editor.putString(user, field.toJson())
            editor.apply()
        }

    fun init(preferences: SharedPreferences) {
        Shared.preferences = preferences
        currentUser = User.fromJson(preferences.getString(user, "{}"))
    }
}

