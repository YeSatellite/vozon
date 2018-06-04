package com.yesat.vozon.ui.auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yesat.vozon.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        v_client.setOnClickListener{
            val i = Intent(this, XSignUpActivity::class.java)
            startActivity(i)
        }

        v_courier.setOnClickListener{
            val i = Intent(this, YSignUpActivity::class.java)
            startActivity(i)
        }
    }
}
