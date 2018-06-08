package com.yesat.vozon.ui.courier.route

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.yesat.vozon.R
import com.yesat.vozon.models.Location
import com.yesat.vozon.models.Route
import com.yesat.vozon.models.Transport
import com.yesat.vozon.ui.courier.transport.TransportListActivity
import com.yesat.vozon.ui.info.LocationActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_route_new.*
import java.util.*

class RouteNewActivity : AppCompatActivity() {

    companion object {
        const val START_POINT_REQUEST_CODE = 54
        const val END_POINT_REQUEST_CODE = 57
        const val TRANSPORT_REQUEST_CODE = 68
    }

    private val route = Route()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_new)
        addBackPress()

        v_transport.setOnClickListener{
            val i = Intent(this, TransportListActivity::class.java)
            startActivityForResult(i, TRANSPORT_REQUEST_CODE)
        }
        v_start_point.setOnClickListener({
            val i = Intent(this@RouteNewActivity, LocationActivity::class.java)
            startActivityForResult(i, START_POINT_REQUEST_CODE)
        })
        v_end_point.setOnClickListener({
            val i = Intent(this@RouteNewActivity,LocationActivity::class.java)
            startActivityForResult(i, END_POINT_REQUEST_CODE)
        })
        v_shipping_date.setOnClickListener{
            val calendar = Calendar.getInstance()
            DatePickerDialog(this@RouteNewActivity,
                    DatePickerDialog.OnDateSetListener {
                        _, year, month, dayOfMonth ->
                        v_shipping_date.content = "$year-$month-$dayOfMonth"
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        v_shipping_time.setOnClickListener{
            TimePickerDialog(this@RouteNewActivity,
                    TimePickerDialog.OnTimeSetListener {
                        _, hourOfDay, minute ->
                        v_shipping_time.content = "$hourOfDay:$minute"
                    }, 0, 0, true).show()
        }
    }


    private fun create(){
        try{
            checkNotNull(route.transport){getString(R.string.is_empty,"transport")}
            check(route.startPoint != null){getString(R.string.is_empty,"start point")}
            check(route.endPoint != null){getString(R.string.is_empty,"end point")}
            route.shippingDate = v_shipping_date.get(getString(R.string.is_empty,"shipping date"))
            route.shippingTime = v_shipping_time.get(getString(R.string.is_empty,"shipping time"))
            route.comment = v_comment.get(getString(R.string.is_empty,"comment"))

            Api.courierService.routesAdd(route).run3(this){
                setResult(Activity.RESULT_OK)
                finish()
            }

        }catch (ex: IllegalStateException){
            snack(ex.message ?: "Unknown error")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                create()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TRANSPORT_REQUEST_CODE -> {
                    route.transport = data!!.get(Transport::class.java)
                    v_transport.text = route.transport!!.fullName
                }
                START_POINT_REQUEST_CODE -> {
                    route.startPoint = data!!.get(Location::class.java)
                    v_start_point.content = route.startPoint!!.getShortName()
                }
                END_POINT_REQUEST_CODE -> {
                    route.endPoint = data!!.get(Location::class.java)
                    v_end_point.content = route.endPoint!!.getShortName()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}











