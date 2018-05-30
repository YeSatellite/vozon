package com.yesat.vozon.ui.auth

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yesat.vozon.R
import com.yesat.vozon.Shared
import com.yesat.vozon.models.User
import com.yesat.vozon.addBackPress
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.include_toolbar.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(v_toolbar)
        addBackPress()

        v_logout.setOnClickListener{
            Shared.currentUser = User()
            setResult(Activity.RESULT_OK)
            this.finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
