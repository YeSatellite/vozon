package com.yesat.vozon.ui.courier.route

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.yesat.vozon.R
import com.yesat.vozon.models.Route
import com.yesat.vozon.ui.ImagePagerAdapter
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_y_route_detail.*


class YRouteDetailActivity : AppCompatActivity() {

    private var route: Route? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_y_route_detail)
        addBackPress()

        route = intent.get(Route::class.java)

        supportActionBar!!.title = route!!.transport!!.modelName

        v_images.adapter = ImagePagerAdapter(
                this,
                listOf(route?.transport?.image1, route?.transport?.image2))

        v_image.src(route?.transport?.type?.icon,R.drawable.tmp_truck)
        v_image.addFilter()
        v_transport.text = route?.transport?.fullName
        v_t_type.text = getString(R.string._o_,route?.transport?.type?.name,route?.transport?.loadTypeName)


        v_date.text = route?.shippingDate?.dateFormat()
        v_start_point.text = route?.startPoint!!.getShortName()
        if (route!!.endPoint != null) v_end_point.text = route!!.endPoint?.getShortName()

        if (!route!!.comment.isNullOrEmpty()) v_comment.text = route!!.comment
        else v_comment.visibility = View.GONE
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
