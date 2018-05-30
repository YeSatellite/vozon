package com.yesat.vozon.ui.client

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.theartofdev.edmodo.cropper.CropImage
import com.yesat.vozon.R
import com.yesat.vozon.compressImage
import com.yesat.vozon.get
import com.yesat.vozon.locationFormat
import com.yesat.vozon.models.Location
import com.yesat.vozon.models.Order
import java.io.File
import java.util.*


class OrderNewActivity : AppCompatActivity() {
    companion object {
        const val START_POINT_REQUEST_CODE = 54
        const val END_POINT_REQUEST_CODE = 57
        const val PAYMENT_TYPE_REQUEST_CODE = 86
    }


    private val images = ArrayList<File>()
    private val order = Order()
    private var imageNew: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_new)

        order.category = intent.get2(Category::class.java).id

        adapter = CustomPagerAdapter(this)
        v_images.adapter = adapter
        v_images.onChange { position ->
            v_image_text.text = "${position+1}/2 image"
        }

        imageNew = v_image_new
        imageNew!!.setOnClickListener({
            CropImage.startPickImageActivity(this)
        })

        v_start_point.setOnClickListener({
            val i = Intent(this@OrderNewActivity,LocationActivity::class.java)
            startActivityForResult(i,START_POINT_REQUEST_CODE)
        })
        v_end_point.setOnClickListener({
            val i = Intent(this@OrderNewActivity,LocationActivity::class.java)
            startActivityForResult(i,END_POINT_REQUEST_CODE)
        })
        v_payment_type.setOnClickListener {
            val i = Intent(this@OrderNewActivity,InfoTmpActivity::class.java)
            Shared.call = Api.infoService.paymentType()
            startActivityForResult(i,PAYMENT_TYPE_REQUEST_CODE)
        }
        v_shipping_date.setOnClickListener{
            val calendar = Calendar.getInstance()
            DatePickerDialog(this@OrderNewActivity,
                    DatePickerDialog.OnDateSetListener {
                        _, year, month, dayOfMonth ->
                        v_shipping_date.text2 = "$year-$month-$dayOfMonth"
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        v_shipping_time.setOnClickListener{
            TimePickerDialog(this@OrderNewActivity,
                    TimePickerDialog.OnTimeSetListener {
                        _, hourOfDay, minute ->
                        v_shipping_time.text2 = "$hourOfDay:$minute"
                    }, 0, 0, true).show()
        }
        v_create.setOnClickListener({
            create()
        })
    }

    private fun create(){
        try{
            order.title = v_title.get(getString(R.string.is_empty,"title"))
            order.comment = v_comment.get("comment is empty")
            val height = v_height.get("height is empty").toFloat()
            val width = v_width.get("width is empty").toFloat()
            val length = v_length.get("length is empty").toFloat()
            order.volume = height*width*length/1000000
            order.mass = v_mass.get("mass is empty").toFloat()
            check(order.startPoint != null){"start point is empty"}
            check(order.endPoint != null){"end point is empty"}
            order.startDetail = v_start_detail.get("Start Detail is empty")
            order.endDetail = v_end_detail.get("Start Detail is empty")
            order.shippingDate = v_shipping_date.get("shipping date is empty")
            order.shippingTime = v_shipping_time.get("shipping time is empty")
            order.acceptPerson = v_accept_person.get("acceptPerson is empty")
            order.acceptPersonContact = v_accept_person.get("acceptPersonContact is empty")
            order.ownerType = when(v_owner_type.checkedRadioButtonId){
                R.id.v_natural_person -> 1
                R.id.v_juridical_person -> 2
                else -> error("select owner type")
            }

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
        val image1 = images[0].toMultiPartImage("image1")
        val image2 = images[1].toMultiPartImage("image2")

        Api.clientService.orderUpdate(id,image1,image2).run2(this,{
            setResult(Activity.RESULT_OK)
            finish()
        },{ _, error ->
            snack(error)
        })
    }























    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> {
                    if (CropImage.isExplicitCameraPermissionRequired(this)) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
                    }
                    else {
                        val imageUri = CropImage.getPickImageResultUri(this, data)
                        images.add(compressImage(imageUri))
                        adapter.notifyDataSetChanged()
                        if (images.size == 2)
                            imageNew!!.visibility = View.GONE
                    }
                }
                START_POINT_REQUEST_CODE -> {
                    order.startPoint = data!!.get(Location::class.java)
                    v_start_point.text2 = locationFormat(order.startPoint!!)
                }
                END_POINT_REQUEST_CODE -> {
                    order.endPoint = data!!.get(Location::class.java)
                    v_end_point.text2 = locationFormat(order.endPoint!!)
                }
                PAYMENT_TYPE_REQUEST_CODE -> {
                    val paymentType =  data!!.get2(InfoTmp::class.java)
                    order.paymentType = paymentType.id
                    v_payment_type.setText(paymentType.name, TextView.BufferType.EDITABLE)
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









