package com.yesat.vozon.ui.courier.transport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yesat.vozon.R
import com.yesat.vozon.models.Transport
import com.yesat.vozon.ui.ImagePagerAdapter
import com.yesat.vozon.utility.addBackPress
import com.yesat.vozon.utility.get
import kotlinx.android.synthetic.main.activity_transport_detail.*


class TransportDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_detail)
        addBackPress()

        val transport = intent.get(Transport::class.java)

        supportActionBar!!.title = transport.number
        v_images.adapter = ImagePagerAdapter(
                this,
                listOf(transport.image1, transport.image2))

        v_length.text = getString(R.string.meter,transport.length)
        v_width.text = getString(R.string.meter,transport.width)
        v_height.text = getString(R.string.meter,transport.height)
        v_number.text = transport.number
        v_mark.text = transport.markName
        v_model.text = transport.modelName
        v_shipping_type.text = transport.shippingTypeName
        v_comment.text = transport.comment

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
