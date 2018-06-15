package com.yesat.vozon.ui.client.active

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.yesat.vozon.R
import com.yesat.vozon.models.User
import com.yesat.vozon.ui.BackPressCompatActivity
import com.yesat.vozon.utility.addBackPress
import com.yesat.vozon.utility.get
import com.yesat.vozon.utility.src
import kotlinx.android.synthetic.main.fragment_courier_profile.*

class CourierProfileActivity : BackPressCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_courier_profile)

        val user = intent.get(User::class.java)

        v_avatar.src(user.avatar,R.drawable.user_placeholder)
        v_transport.text = user.name
        v_courier_type.text = user.courierTypeName
        v_about.text = user.about
        v_city.text = user.city?.getShortName() ?: ""
        v_phone.text = user.phone
        v_experience.text = user.experience.toString()
        v_setting.visibility = View.GONE

    }
}
