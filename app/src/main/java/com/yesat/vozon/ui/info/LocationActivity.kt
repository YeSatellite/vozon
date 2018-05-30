package com.yesat.vozon.ui.info

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.car.ui.info.CityFragment
import com.yesat.vozon.Api
import com.yesat.vozon.R
import com.yesat.vozon.norm
import kotlinx.android.synthetic.main.include_toolbar.*

class LocationActivity : AppCompatActivity() {

    var state = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        setSupportActionBar(v_toolbar)

        next(0)
    }

    fun next(id:Long){
        norm(state.toString())
        val selectedFragment = when(state){
            0 -> LocationFragment(Api.infoService.country())
            1 -> LocationFragment(Api.infoService.region(id))
            2 -> CityFragment(Api.infoService.city(id))
            else -> return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.v_container, selectedFragment)
        transaction.commit()
        state++
    }
}
