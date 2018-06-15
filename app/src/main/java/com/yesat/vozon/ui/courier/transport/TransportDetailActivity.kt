package com.yesat.vozon.ui.courier.transport

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.yesat.vozon.R
import com.yesat.vozon.models.Transport
import com.yesat.vozon.ui.BackPressCompatActivity
import com.yesat.vozon.ui.ImagePagerAdapter
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_transport_detail.*


class TransportDetailActivity : BackPressCompatActivity() {
    var transport: Transport? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_detail)


        transport = intent.get(Transport::class.java)

        supportActionBar!!.title = transport!!.number
        v_images.adapter = ImagePagerAdapter(
                this,
                listOf(transport!!.image1, transport!!.image2))

        v_length.text = getString(R.string.meter,transport!!.length)
        v_width.text = getString(R.string.meter,transport!!.width)
        v_height.text = getString(R.string.meter,transport!!.height)
        v_number.text = transport!!.number
        v_mark.text = transport!!.markName
        v_model.text = transport!!.modelName
        v_shipping_type.text = transport!!.shippingTypeName
        v_load_type.text = transport!!.loadTypeName
        v_comment.text = transport!!.comment

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_remove, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.remove -> {
                Api.courierService.transportDelete(transport!!.id!!).run2(this,{
                },{ code, error ->
                    if(code == 0){
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    else{
                        snack(error)
                    }
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
