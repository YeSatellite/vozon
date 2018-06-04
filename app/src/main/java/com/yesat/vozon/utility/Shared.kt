package com.yesat.vozon.utility

import android.content.SharedPreferences
import com.yesat.vozon.R
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

    var theme: Int = R.style.AppTheme
    var themeNo: Int = R.style.AppTheme_NoActionBar

    fun setTheme(){
        when (currentUser.type){
            User.CLIENT -> {
                Shared.theme = R.style.AppTheme
                Shared.themeNo = R.style.AppTheme_NoActionBar
            }
            User.COURIER -> {
                Shared.theme = R.style.AppThemeDark
                Shared.themeNo = R.style.AppThemeDark_NoActionBar
            }
        }
    }
}

