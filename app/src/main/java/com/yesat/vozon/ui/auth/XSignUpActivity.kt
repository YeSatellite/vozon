package com.yesat.vozon.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.theartofdev.edmodo.cropper.CropImage
import com.yesat.vozon.*
import com.yesat.vozon.models.Location
import com.yesat.vozon.models.User
import com.yesat.vozon.ui.info.LocationActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_client_signup.*
import kotlinx.android.synthetic.main.include_sign_up_main.*
import java.io.File


class XSignUpActivity : AppCompatActivity() {

    companion object {
        const val CITY_REQUEST_CODE = 86
        const val FINISH_REQUEST_CODE = 100
        const val IMAGE_REQUEST_CODE = 196
    }

    private val user = User()
    private var image: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_signup)
        addBackPress()

        user.type = User.CLIENT

        v_city.setOnClickListener({
            val i = Intent(this@XSignUpActivity, LocationActivity::class.java)
            startActivityForResult(i, CITY_REQUEST_CODE)
        })
        v_upload_image.setOnClickListener{
            startActivityForResult(CropImage.getPickImageChooserIntent(this),
                    IMAGE_REQUEST_CODE)
        }



    }

    private fun done(){
        try{
            user.phone = v_phone.get("phone is empty")
            user.name = v_transport.get("name is empty")
            user.about = v_about.get("dob is empty")
            checkNotNull(user.city){"city is empty"}
            checkNotNull(image){"avatar is empty"}


            val phone = user.phone!!.toMultiPart()
            val name = user.name!!.toMultiPart()
            val city = user.city!!.id.toString().toMultiPart()
            val about = user.about!!.toMultiPart()
            val type = user.type!!.toMultiPart()
            val image = image!!.toMultiPartImage("avatar")

            Api.authService.register(phone,name,city,about,type,image).run2(this,{
                norm("hello")
                val i = Intent(this, LoginActivity::class.java)
                i.put(user.phone!!)
                startActivityForResult(i,FINISH_REQUEST_CODE)
            },{ _, error ->
                snack(error)
            })


        }catch (ex: IllegalStateException){
            snack(ex.message ?: "Unknown error")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CITY_REQUEST_CODE -> {
                    val location = data!!.get(Location::class.java)
                    user.city = location
                    v_city.content = location.getShortName()
                }
                IMAGE_REQUEST_CODE -> {
                    val imageUri = CropImage.getPickImageResultUri(this, data)
                    v_image.setImageURI(imageUri)
                    image = compressImage(imageUri)
                }
            }
        }

        if(requestCode == FINISH_REQUEST_CODE){
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                done()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
