package com.yesat.vozon.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.yesat.vozon.utility.Api
import com.yesat.vozon.R
import com.yesat.vozon.utility.Shared
import com.yesat.vozon.utility.run2
import com.yesat.vozon.utility.clientOrCourier
import com.yesat.vozon.utility.get
import com.yesat.vozon.utility.snack
import kotlinx.android.synthetic.main.activity_login.*
import com.yesat.vozon.services.MyFirebaseInstanceIDService
import com.yesat.vozon.ui.BackPressCompatActivity


class LoginActivity : BackPressCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val token = Shared.token
        if (token.isEmpty()){
            val intent = Intent(this, MyFirebaseInstanceIDService::class.java)
            startService(intent)
        }


        val phone = intent.get(String::class.java)
        v_pin.setOnPinEnteredListener { str ->
            if (str.length == 4) {
                val sms = v_pin.get()
                login(phone,sms)
            }
        }
    }

    private fun login(phone:String, smsCode:String){
        val token = Shared.token
        if (token.isEmpty()){
            snack("Try later")
            return
        }
        val map = hashMapOf(
                "phone" to phone,
                "sms_code" to smsCode,
                "phone_type" to "Android",
                "registration_id" to token)
        Api.authService.login(map).run2(this,{ user ->
            Shared.currentUser = user
            val status = clientOrCourier()
            startActivityForResult(Intent(this, status),77)
        },{ _, error ->
            snack(error)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        finish()
    }
}