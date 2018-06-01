package com.yesat.vozon.ui.info

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.vozon.utility.Api
import com.yesat.vozon.R
import com.yesat.vozon.utility.norm

class LocationActivity : AppCompatActivity() {
    companion object {
        const val EXTRA = "extra"
    }

    var state = 0

    val i = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        next(0)
    }

    fun next(id: Long) {
        norm(state.toString())
        val selectedFragment = when (state) {
            0 -> LocationFragment(Api.infoService.country())
            1 -> LocationFragment(Api.infoService.region(id))
            2 -> CityFragment(Api.infoService.city(id))
            3 -> {
                if (intent.getStringExtra(EXTRA) == EXTRA)
                    LocationExtraFragment()
                else {
                    setResult(Activity.RESULT_OK, i)
                    finish()
                    return
                }
            }
            4 -> {
                setResult(Activity.RESULT_OK, i)
                finish()
                return
            }
            else -> return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.v_container, selectedFragment)
        transaction.commit()
        state++
    }
}
