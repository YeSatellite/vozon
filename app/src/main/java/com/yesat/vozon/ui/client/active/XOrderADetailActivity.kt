package com.yesat.vozon.ui.client.active

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import com.yesat.vozon.R
import com.yesat.vozon.models.Order
import com.yesat.vozon.ui.BackPressCompatActivity
import com.yesat.vozon.ui.ImagePagerAdapter
import kotlinx.android.synthetic.main.activity_order_a_detail.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.view.LayoutInflater
import android.view.View
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.item_rating.view.*


class XOrderADetailActivity : BackPressCompatActivity() {

    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_a_detail)

        order = intent.get(Order::class.java)
        norm(order.toString())

        supportActionBar!!.title = order!!.title

        v_images.adapter = ImagePagerAdapter(this,listOf(order!!.image1,order!!.image2))

        v_date.text = order!!.shippingDate!!.dateFormat()
        v_start_point.text = order!!.startPoint!!.getShortName(order!!.startDetail!!)
        v_end_point.text = order!!.endPoint!!.getShortName(order!!.endDetail!!)

        v_volume.text = getString(R.string.meter3,order!!.width!! * order!!.height!!*order!!.length!!)
        v_mass.text = if(order!!.mass!!>1000){
            getString(R.string.kg,order!!.mass!! / 1000)
        }else{
            getString(R.string.g,order!!.mass!!)
        }
        v_price.text = getString(R.string.tenge,order!!.price)

        v_position.text = order!!.startPoint!! - order!!.endPoint!!
        v_category.text = order!!.categoryName
        v_payment_type.text = order!!.paymentTypeName

        v_comment.text = order!!.comment

        val offer = order!!.offer!!
        v_image.src(offer.transport?.typeIcon,R.drawable.tmp_truck)
        v_transport.text = offer.transport?.fullName
        v_t_type.text = offer.transport?.typeName
        v_price2.text = getString(R.string.tenge, offer.price!!.toFloat())
        v_payment_type2.text = offer.paymentTypeName
        v_other_service.text = offer.otherServiceName
        v_shipping_type.text = offer.shippingTypeName
        v_comment2.text = offer.comment

        v_button.text = getString(R.string.done)
        v_button.setOnClickListener{
            val v = View.inflate(this,R.layout.item_rating,null)
            val popDialog = AlertDialog.Builder(this)
            popDialog.setTitle("Оцените работу курьера")
            popDialog.setView(v)
            popDialog.setPositiveButton(android.R.string.ok, { dialog, _ ->
                Api.clientService.orderDone(order!!.id!!,v.v_rating.progress)
                        .run3(this){
                            setResult(RESULT_OK)
                            finish()
                        }
                        dialog.dismiss()
            }).setNegativeButton(android.R.string.cancel, { dialog, _ -> dialog.cancel() })

            popDialog.create()
            popDialog.show()


        }

    }
}
