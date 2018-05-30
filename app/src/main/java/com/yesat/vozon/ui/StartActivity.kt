package com.yesat.vozon.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.yesat.vozon.*
import com.yesat.vozon.models.User
import com.yesat.vozon.ui.auth.SendSmsActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        Shared.init(PreferenceManager.getDefaultSharedPreferences(applicationContext))

        if (Shared.currentUser.token == null) {
            next()
            return
        }

        val test = when (Shared.currentUser.type) {
            User.CLIENT -> Api.clientService.test()
            User.COURIER -> Api.courierService.test()
            else -> {
                Shared.currentUser = User()
                next()
                return
            }
        }

        test.run2(this,
                { body ->
                    body.token = Shared.currentUser.token
                    Shared.currentUser = body
                    next()
                },
                { code, _ ->
                    if (code == 200) {
                        Shared.currentUser = User()
                        next()
                    }
                })
    }

    fun next() {
        val i = Intent(this, SendSmsActivity::class.java)
        val action = intent.getStringExtra(Shared.action)
        norm(action)
        i.putExtra(Shared.action, action)
        startActivityForResult(i, 45)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        finish()
    }
}