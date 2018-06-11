package com.yesat.vozon.ui.courier.order

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.vozon.R
import com.yesat.vozon.models.Order
import com.yesat.vozon.ui.ImagePagerAdapter
import com.yesat.vozon.ui.client.route.XRouteDetailActivity
import com.yesat.vozon.ui.client.route.XRouteDetailActivity.Companion.REQUEST_PHONE_CALL
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_y_order_a_detail.*


class YOrderADetailActivity : AppCompatActivity() {

    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_y_order_a_detail)
        addBackPress()

        order = intent.get(Order::class.java)
        norm(order.toString())

        supportActionBar!!.title = order!!.title

        v_images.adapter = ImagePagerAdapter(this,listOf(order!!.image1,order!!.image2))
        

        v_avatar.src(order?.owner?.avatar,R.drawable.user_placeholder)
        v_name.text = order?.owner?.name
        v_date.text = order!!.shippingDate!!.dateFormat(order!!.shippingTime!!)

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
        v_price2.text = offer.price.toString()
        v_payment_type2.text = offer.paymentTypeName
        v_other_service.text = offer.otherServiceName
        v_shipping_type.text = offer.shippingTypeName
        v_comment2.text = offer.comment

        v_name2.text = order?.acceptPerson

        v_call.setOnClickListener{
            offer.transport!!.owner!!.callIntent()
            if(askPermission(Manifest.permission.CALL_PHONE, REQUEST_PHONE_CALL))
                startActivity(order?.callIntent())
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


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            XRouteDetailActivity.REQUEST_PHONE_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(order!!.offer!!.transport!!.owner!!.callIntent())
                }
            }
        }
    }
}
