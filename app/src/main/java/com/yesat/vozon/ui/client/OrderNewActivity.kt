package com.yesat.vozon.ui.client

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.theartofdev.edmodo.cropper.CropImage
import com.yesat.vozon.R
import com.yesat.vozon.models.Category
import com.yesat.vozon.models.InfoTmp
import com.yesat.vozon.models.Location
import com.yesat.vozon.models.Order
import com.yesat.vozon.ui.info.InfoTmpActivity
import com.yesat.vozon.ui.info.LocationActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_order_new.*
import java.io.File


class OrderNewActivity : AppCompatActivity() {
    companion object {
        const val START_POINT_REQUEST_CODE = 54
        const val END_POINT_REQUEST_CODE = 57
        const val PAYMENT_TYPE_REQUEST_CODE = 86
    }


    private val order = Order()
    private var image1: File? = null
    private var image2: File? = null
    private var imageCur = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_new)
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE,750)

        order.category = intent.get(Category::class.java).id

        v_image1!!.setOnClickListener({
            imageCur = 1
            CropImage.startPickImageActivity(this)
        })
        v_image2!!.setOnClickListener({
            imageCur = 2
            CropImage.startPickImageActivity(this)
        })

        v_start_point.setOnClickListener({
            val i = Intent(this@OrderNewActivity, LocationActivity::class.java)
            i.putExtra(LocationActivity.EXTRA,LocationActivity.EXTRA)
            startActivityForResult(i,START_POINT_REQUEST_CODE)
        })
        v_end_point.setOnClickListener({
            val i = Intent(this@OrderNewActivity,LocationActivity::class.java)
            i.putExtra(LocationActivity.EXTRA,LocationActivity.EXTRA)
            startActivityForResult(i,END_POINT_REQUEST_CODE)
        })
        v_shipping_date.setOnClickListener(setDateListener(this))
        v_shipping_time.setOnClickListener(setTimeListener(this))
        v_transport.setOnClickListener {
            val i = Intent(this@OrderNewActivity, InfoTmpActivity::class.java)
            Shared.call = Api.infoService.paymentType()
            startActivityForResult(i,PAYMENT_TYPE_REQUEST_CODE)
        }
    }

    private fun create(){
        try{
            order.title = v_title.get("title is empty")
            order.comment = v_comment.get("comment is empty")
            order.height = v_height.get("height is empty").toFloat()
            order.width = v_width.get("width is empty").toFloat()
            order.length = v_length.get("length is empty").toFloat()
            order.mass = v_mass.get("mass is empty").toFloat()
            check(order.startPoint != null){"start point is empty"}
            check(order.endPoint != null){"end point is empty"}
            order.shippingDate = v_shipping_date.get("shipping date is empty")
            order.shippingTime = v_shipping_time.get("shipping time is empty")
            order.price = v_price.get(getString(R.string.is_empty,"цена")).toFloat()
            order.acceptPerson = v_accept_person.get("acceptPerson is empty")
            order.acceptPersonContact = v_accept_person.get("acceptPersonContact is empty")


            Api.clientService.orderAdd(order).run2(this,{ body ->
                updateImage(body.id!!)
            },{ _, error ->
                snack(error)
            })

        }catch (ex: IllegalStateException){
            snack(ex.message ?: "Unknown error")
        }
    }

    private fun updateImage(id: Long) {
        val image1 = image1!!.toMultiPartImage("image1")
        val image2 = image2!!.toMultiPartImage("image2")

        Api.clientService.orderUpdate(id,image1,image2).run2(this,{
            setResult(Activity.RESULT_OK)
            finish()
        },{ _, error ->
            snack(error)
        })
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
                CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> {
                    val imageUri = CropImage.getPickImageResultUri(this, data)
                    when(imageCur){
                        1 -> {
                            image1 = compressImage(imageUri)
                            v_image1.setImageURI(imageUri)
                        }
                        2 -> {
                            image2 = compressImage(imageUri)
                            v_image2.setImageURI(imageUri)
                        }
                    }

                }
                START_POINT_REQUEST_CODE -> {
                    order.startPoint = data!!.get(Location::class.java)
                    order.startDetail = data.getStringExtra(LocationActivity.EXTRA)
                    v_start_point.content = order.startPoint!!.getShortXName(order.startDetail!!)
                }
                END_POINT_REQUEST_CODE -> {
                    order.endPoint = data!!.get(Location::class.java)
                    order.endDetail = data.getStringExtra(LocationActivity.EXTRA)
                    v_end_point.content = order.endPoint!!.getShortXName(order.endDetail!!)
                }
                PAYMENT_TYPE_REQUEST_CODE -> {
                    val paymentType =  data!!.get(InfoTmp::class.java)
                    order.paymentType = paymentType.id
                    v_transport.setText(paymentType.name, TextView.BufferType.EDITABLE)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            750 -> if ((grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED))
                finish()
            else ->{}

        }
    }
}









