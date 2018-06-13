package com.yesat.vozon.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.theartofdev.edmodo.cropper.CropImage
import com.yesat.vozon.*
import com.yesat.vozon.models.Country
import com.yesat.vozon.models.Location
import com.yesat.vozon.models.User
import com.yesat.vozon.ui.CodeAdapter
import com.yesat.vozon.ui.info.LocationActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_client_signup.*
import kotlinx.android.synthetic.main.include_sign_up_main.*
import java.io.File


class YSignUpActivity : AppCompatActivity() {

    companion object {
        const val CITY_REQUEST_CODE = 86
        const val FINISH_REQUEST_CODE = 100
        const val IMAGE_REQUEST_CODE = 196
    }

    private val user = User()
    private var image: File? = null

    var phoneOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_signup)
        addBackPress()

        user.type = User.COURIER

        v_city.setOnClickListener({
            val i = Intent(this@YSignUpActivity, LocationActivity::class.java)
            startActivityForResult(i, CITY_REQUEST_CODE)
        })
        v_upload_image.setOnClickListener{
            startActivityForResult(CropImage.getPickImageChooserIntent(this),
                    IMAGE_REQUEST_CODE)
        }

        var countries: List<Country>
        Api.infoService.countryPhone().run3(this,{
            countries = it
            after(countries)
        })
    }


    private fun after(countries: List<Country>) {
        var country: Country
        val adapter = CodeAdapter(this, countries)
        var listener: MaskedTextChangedListener? = null

        v_spinner.adapter = adapter
        v_spinner.setSelection(0)
        v_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                v_phone_number.text.clear()
                country = countries[position]
                v_phone_number.removeTextChangedListener(listener)
                listener = MaskedTextChangedListener(
                        country.phoneMask!!,
                        v_phone_number,
                        object : MaskedTextChangedListener.ValueListener {
                            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                                user.phone = country.phoneCode + extractedValue
                                phoneOk = maskFilled
                            }
                        }
                )
                v_phone_number.addTextChangedListener(listener)
                v_phone_number.onFocusChangeListener = listener
            }

        }
        v_spinner.onItemSelectedListener
                .onItemSelected(null, null, v_spinner.selectedItemPosition, 0)
    }

    private fun next(){
        try{
            user.phone = v_phone_number.get("phone is empty")
            user.name = v_transport.get("name is empty")
            checkNotNull(user.city){"city is empty"}
            checkNotNull(image){"avatar is empty"}

            val i = Intent(this,YSignUpNextActivity::class.java)
            i.put(user)
            i.put(image!!)
            startActivity(i)


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
        menuInflater.inflate(R.menu.menu_next, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.next -> {
                next()
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
