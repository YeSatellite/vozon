package com.yesat.vozon.ui.info

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yesat.vozon.R
import com.yesat.vozon.utility.addBackPress
import kotlinx.android.synthetic.main.activity_terms.*

class TermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        addBackPress()

        v_web.loadUrl("http://188.166.50.157:8000/info/terms")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
