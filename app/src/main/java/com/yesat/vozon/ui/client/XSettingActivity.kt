package com.yesat.vozon.ui.client

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yesat.vozon.R
import com.yesat.vozon.utility.Shared
import com.yesat.vozon.models.User
import com.yesat.vozon.utility.addBackPress
import kotlinx.android.synthetic.main.activity_setting.*

class XSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        addBackPress()

        v_logout.setOnClickListener{
            Shared.currentUser = User()
            setResult(Activity.RESULT_OK)
            this.finish()
        }

        v_edit.setOnClickListener{
            startActivity(Intent(this,XProfileUpdateActivity::class.java))
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
