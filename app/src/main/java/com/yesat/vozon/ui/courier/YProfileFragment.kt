package com.yesat.vozon.ui.courier

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.yesat.vozon.R
import com.yesat.vozon.ui.auth.SettingActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.fragment_courier_profile.view.*

class YProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_courier_profile, container, false)

        val user = Shared.currentUser
        norm(user.avatar)

        v.v_avatar.src(user.avatar,R.drawable.user_placeholder)
        v.v_transport.text = user.name
        v.v_courier_type.text = user.courierTypeName
        v.v_about.text = user.about
        v.v_city.text = user.city?.getShortName() ?: ""
        v.v_phone.text = user.phone
        v.v_experience.text = user.experience.toString()
        v.v_setting.setOnClickListener{
            startActivityForResult(Intent(context, SettingActivity::class.java),26)
        }
        v.v_setting.addFilter(R.attr.textColorLarge)
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 26){
            if (resultCode == Activity.RESULT_OK){
                activity?.finish()
            }
        }
    }
}
