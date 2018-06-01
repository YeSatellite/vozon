package com.yesat.vozon.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.vozon.*
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_send_sms.*

class SendSmsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_sms)

        authTest()

        v_send_sms.setOnClickListener {
            try {
                val phone = v_phone_number.get("phone is empty")
                sendSms(phone)
            }catch (ex: IllegalStateException){
                snack(ex.message ?: "Unknown error")
            }
        }

        v_sign_up.setOnClickListener {
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        }
    }

    private fun sendSms(phone: String){
        val map = hashMapOf("phone" to phone)
        Api.authService.sentSms(map).run2(this,{
            val i = Intent(this, LoginActivity::class.java)
            i.put(phone)
            startActivity(i)
        },{ _, error ->
            snack(error)
        })
    }


    private fun authTest() {
        if (Shared.currentUser.token != null){
            val status = clientOrCourier()
            val i = Intent(this, status)
            val action = intent.getStringExtra(Shared.action)
            norm(action)
            i.putExtra(Shared.action,action)
            startActivityForResult(i,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Shared.currentUser.token != null)
            finish()
    }
}
