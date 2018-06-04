package com.yesat.vozon.ui.courier.order

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.vozon.R
import com.yesat.vozon.models.Offer
import com.yesat.vozon.models.Order
import com.yesat.vozon.ui.ImagePagerAdapter
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_order_detail_b.*


class YOrderPDetailActivity : AppCompatActivity() {

    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_b)
        addBackPress()

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
        v_transport.text = order!!.paymentTypeName

        v_comment.text = order!!.comment

        v_button.text = "Предложить цену"
        v_button.setOnClickListener{
            val i = Intent(this,OfferNewActivity::class.java)
            i.put(order!!)
            startActivityForResult(i,45)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
