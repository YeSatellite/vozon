package com.yesat.vozon.ui.client


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.R
import com.yesat.vozon.utility.Shared
import com.yesat.vozon.ui.auth.SettingActivity
import com.yesat.vozon.utility.norm
import com.yesat.vozon.utility.src
import kotlinx.android.synthetic.main.fragment_xprofile.view.*

class XProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_xprofile, container, false)

        val user = Shared.currentUser
        norm(user.avatar)

        v.v_avatar.src = user.avatar
        v.v_name.text = user.name
        v.v_about.text = user.about
        v.v_city.text = user.city?.getShortName() ?: ""
        v.v_phone.text = user.phone
        v.v_dob.text = user.dob

        v.v_setting.setOnClickListener{
            startActivityForResult(Intent(context,SettingActivity::class.java),26)
        }

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
