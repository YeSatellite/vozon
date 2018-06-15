package com.yesat.vozon.ui.client.posted

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.yesat.vozon.R
import com.yesat.vozon.models.Order
import com.yesat.vozon.ui.BackPressCompatActivity
import com.yesat.vozon.ui.ImagePagerAdapter
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_order_posted_detail.*


class XOrderPDetailActivity : BackPressCompatActivity() {
    var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_posted_detail)


        val order = intent.get(Order::class.java)
        this.order = order

        supportActionBar!!.title = order.title

        v_images.adapter = ImagePagerAdapter(this,listOf(order.image1, order.image2)
                .filter { it != null })

        v_date.text = order.shippingDate!!.dateFormat()
        v_start_point.text = order.startPoint!!.getShortName(order.startDetail!!)
        v_end_point.text = order.endPoint!!.getShortName(order.endDetail!!)

        v_volume.text = getString(R.string.meter3,order.width!! * order.height!!*order.length!!)
        v_mass.text = if(order.mass!!>1000){
            getString(R.string.kg, order.mass!! / 1000)
        }else{
            getString(R.string.g, order.mass!!)
        }
        v_price.text = getString(R.string._s_,order.price.toString(),order.currency)

        v_t_type.text = order.startPoint!! - order.endPoint!!
        v_category.text = order.categoryName
        v_transport.text = order.paymentTypeName

        v_comment.text = order.comment

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_remove, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.remove -> {
                Api.clientService.orderDelete(order!!.id!!).run2(this,{
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
