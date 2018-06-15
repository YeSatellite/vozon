package com.yesat.vozon.ui.courier.transport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.theartofdev.edmodo.cropper.CropImage
import com.yesat.vozon.R
import com.yesat.vozon.models.InfoTmp
import com.yesat.vozon.models.Transport
import com.yesat.vozon.ui.info.InfoTmpActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_transport_new.*
import java.io.File

class TransportNewActivity : AppCompatActivity() {

    companion object {
        const val MARK_REQUEST_CODE = 57
        const val MODEL_REQUEST_CODE = 84
        const val SHIPPING_TYPE_REQUEST_CODE = 89
        const val LOAD_TYPE_REQUEST_CODE = 96
        const val IMAGE1_REQUEST_CODE = 12
        const val IMAGE2_REQUEST_CODE = 13
    }

    private val transport = Transport()
    private val images = Array<File?>(2){null}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_new)

        v_mark.setOnClickListener {
            val i = Intent(this@TransportNewActivity, InfoTmpActivity::class.java)
            Shared.call= Api.infoService.tMark()
            startActivityForResult(i, MARK_REQUEST_CODE)
        }
        v_model.setOnClickListener {
            val i = Intent(this@TransportNewActivity, InfoTmpActivity::class.java)
            Shared.call= Api.infoService.tModel(transport.mark ?: run {
                snack("Choose mark")
                return@setOnClickListener
            })
            startActivityForResult(i, MODEL_REQUEST_CODE)
        }

        v_shipping_type.setOnClickListener {
            val i = Intent(this@TransportNewActivity, InfoTmpActivity::class.java)
            Shared.call= Api.infoService.tShippingType()
            startActivityForResult(i, SHIPPING_TYPE_REQUEST_CODE)
        }

        v_load_type.setOnClickListener {
            val i = Intent(this@TransportNewActivity, InfoTmpActivity::class.java)
            Shared.call= Api.infoService.tLoadType()
            startActivityForResult(i, LOAD_TYPE_REQUEST_CODE)
        }

        v_image1.setOnClickListener{
            startActivityForResult(CropImage.getPickImageChooserIntent(this),
                    IMAGE1_REQUEST_CODE)
        }
        v_image2.setOnClickListener {
            startActivityForResult(CropImage.getPickImageChooserIntent(this),
                    IMAGE2_REQUEST_CODE)
        }
    }


    private fun create(){
        try{
            checkNotNull(images[0]){"image1  is empty"}
            checkNotNull(images[1]){"image2  is empty"}
            transport.number = v_number.get("number is empty")
            checkNotNull(transport.model){"model is empty"}
            transport.height = v_height.get("height is empty").toFloat()
            transport.width = v_width.get("width is empty").toFloat()
            transport.length = v_length.get("length is empty").toFloat()
            checkNotNull(transport.shippingType){"shipping type id empty"}
            checkNotNull(transport.loadType){getString(R.string.is_empty,getString(R.string.load_type))}
            transport.comment = v_comment.get()
            Api.courierService.transportsAdd(transport).run3(this){ body ->
                updateImage(body.id!!)
            }

        }catch (ex: IllegalStateException){
            snack(ex.message ?: getString(R.string.something_went_wrong))
        }
    }

    private fun updateImage(id: Long) {
        val image1 = images[0]!!.toMultiPartImage("image1")
        val image2 = images[1]!!.toMultiPartImage("image2")

        Api.courierService.transportsUpdate(id,image1,image2).run2(this,{
            setResult(Activity.RESULT_OK, Intent())
            finish()
        },{ _, error ->
            snack(error)
        })
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                MARK_REQUEST_CODE -> {
                    val mark =  data!!.get(InfoTmp::class.java)
                    transport.mark = mark.id
                    v_mark.content = mark.name ?: ""
                }
                MODEL_REQUEST_CODE -> {
                    val model =  data!!.get(InfoTmp::class.java)
                    transport.model = model.id
                    v_model.content = model.name ?: ""
                }
                SHIPPING_TYPE_REQUEST_CODE -> {
                    val shippingType =  data!!.get(InfoTmp::class.java)
                    transport.shippingType = shippingType.id
                    v_shipping_type.content = shippingType.name ?: ""
                }
                LOAD_TYPE_REQUEST_CODE -> {
                    val loadType =  data!!.get(InfoTmp::class.java)
                    transport.loadType = loadType.id
                    v_load_type.content = loadType.name ?: ""
                }
                IMAGE1_REQUEST_CODE -> {
                    val imageUri = CropImage.getPickImageResultUri(this, data)
                    v_image1.setImageURI(imageUri)
                    images[0] = compressImage(imageUri)
                }
                IMAGE2_REQUEST_CODE -> {
                    val imageUri = CropImage.getPickImageResultUri(this, data)
                    v_image2.setImageURI(imageUri)
                    images[1] = compressImage(imageUri)
                }
            }
        }
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

}











