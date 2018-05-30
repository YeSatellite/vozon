package com.yesat.vozon.ui.client

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import com.yesat.vozon.R
import com.yesat.vozon.dateFormat
import com.yesat.vozon.models.Order
import com.yesat.vozon.get
import com.yesat.vozon.norm
import com.yesat.vozon.ui.ImagePagerAdapter
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.util.stream.Collectors.toList


class XOrderDetailActivity : AppCompatActivity() {

    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        setSupportActionBar(v_toolbar)

        order = intent.get(Order::class.java)
        norm(order.toString())

        supportActionBar!!.title = order!!.title

        v_images.adapter = ImagePagerAdapter(this,listOf(order!!.image1,order!!.image2))

        v_date.text = order!!.shippingDate!!.dateFormat()
        v_start_point.text = order!!.startPoint!!.getShortName(order!!.startDetail!!)
        v_end_point.text = order!!.endPoint!!.getShortName(order!!.endDetail!!)

        v_volume.text = (order!!.width!! * order!!.height!!*order!!.length!!).toString()
        v_mass.text = order!!.mass.toString()

        v_position.text = order!!.startPoint!! - order!!.endPoint!!
        v_category.text = order!!.categoryName
        v_payment_type.text = order!!.paymentTypeName

        v_comment.text = order!!.comment

    }
}
