package com.yesat.vozon.ui.client

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.vozon.R
import com.yesat.vozon.norm
import kotlinx.android.synthetic.main.activity_xmain.*

class XMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xmain)
        v_navigation.itemIconTintList = null
        v_navigation.enableAnimation(false)
        v_navigation.enableShiftingMode(false)
        v_navigation.enableItemShiftingMode(false)
        v_navigation.setTextVisibility(false)

        v_navigation.setOnNavigationItemSelectedListener {item ->
            val selectedFragment = when (item.itemId) {
                R.id.navigation_home -> XProfileFragment()
                R.id.navigation_dashboard -> XOrderFragment()
                R.id.navigation_notifications -> XProfileFragment()
                else -> XProfileFragment()
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.v_container, selectedFragment)
            transaction.commit()
            true}

        norm("hello")
    }
}
