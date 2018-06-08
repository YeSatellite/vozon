package com.yesat.vozon.ui.client.posted

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.vozon.R
import com.yesat.vozon.utility.dateFormat
import com.yesat.vozon.models.Order
import com.yesat.vozon.utility.get
import com.yesat.vozon.utility.norm
import com.yesat.vozon.ui.ImagePagerAdapter
import com.yesat.vozon.utility.addBackPress
import kotlinx.android.synthetic.main.activity_order_posted_detail.*


class XOrderPDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_posted_detail)
        addBackPress()

        val order = intent.get(Order::class.java)
        norm(order.toString())

        supportActionBar!!.title = order.title

        v_images.adapter = ImagePagerAdapter(this,listOf(order.image1, order.image2))

        v_date.text = order.shippingDate!!.dateFormat()
        v_start_point.text = order.startPoint!!.getShortName(order.startDetail!!)
        v_end_point.text = order.endPoint!!.getShortName(order.endDetail!!)

        v_volume.text = (order.width!! * order.height!!*order.length!!).toString()
        v_mass.text = order.mass.toString()

        v_t_type.text = order.startPoint!! - order.endPoint!!
        v_category.text = order.categoryName
        v_transport.text = order.paymentTypeName

        v_comment.text = order.comment

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
