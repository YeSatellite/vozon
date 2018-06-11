package com.yesat.vozon.ui.courier

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.vozon.ui.courier.transport.TransportListFragment
import com.yesat.vozon.R
import com.yesat.vozon.ui.courier.order.YOrderFragment
import com.yesat.vozon.ui.courier.route.YRouteListFragment
import com.yesat.vozon.utility.Shared
import kotlinx.android.synthetic.main.activity_courier_main.*

class YMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Shared.setTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courier_main)
        v_navigation.itemIconTintList = null
        v_navigation.enableAnimation(false)
        v_navigation.enableShiftingMode(false)
        v_navigation.enableItemShiftingMode(false)
        v_navigation.setTextVisibility(false)

        v_navigation.setOnNavigationItemSelectedListener {item ->
            val selectedFragment = when (item.itemId) {
                R.id.m_route -> YRouteListFragment()
                R.id.m_transport -> TransportListFragment()
                R.id.m_order -> YOrderFragment()
                R.id.m_profile -> YProfileFragment()
                else -> YProfileFragment()
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.v_container, selectedFragment)
            transaction.commit()
            true}

        v_navigation.selectedItemId = R.id.m_route
    }
}
