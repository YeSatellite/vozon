package com.yesat.vozon.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.yesat.vozon.utility.Api
import com.yesat.vozon.R
import com.yesat.vozon.models.User
import com.yesat.vozon.utility.run2
import com.yesat.vozon.utility.toMultiPartImage
import com.yesat.vozon.utility.addBackPress
import com.yesat.vozon.utility.get
import com.yesat.vozon.utility.put
import com.yesat.vozon.utility.snack
import kotlinx.android.synthetic.main.activity_client_signup_next.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File


class XSignUpNextActivity : AppCompatActivity() {

    private var user: User? = null
    private var image: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_signup_next)
        addBackPress()

        user = intent.get(User::class.java)
        image = intent.get(File::class.java)

    }

    private fun next(){
        try{
            user!!.courier_type = when(v_courier_type.checkedRadioButtonId) {
                R.id.v_natural_person -> 1
                R.id.v_juridical_person -> 2
                else -> error("select courier type")
            }
            user!!.experience = v_experience.get("experience is empty").toLong()

            val formData = MediaType.parse("multipart/form-data")
            val phone = RequestBody.create(formData, user!!.phone!!)
            val name = RequestBody.create(formData, user!!.name!!)
            val city = RequestBody.create(formData, user!!.city!!.id.toString())
            val dob = RequestBody.create(formData, user!!.dob!!)
            val about = RequestBody.create(formData, user!!.dob!!)
            val type = RequestBody.create(formData, user!!.type!!)
            val image = image!!.toMultiPartImage("avatar")
            val courierType = RequestBody.create(formData, user!!.courier_type!!.toString())
            Api.authService.register(phone,name,city,about,dob,type,image).run2(this,{
                val i = Intent(this, LoginActivity::class.java)
                i.put(user!!.phone!!)
                startActivityForResult(i, XSignUpActivity.FINISH_REQUEST_CODE)
            },{ _, error ->
                snack(error)
            })


        }catch (ex: IllegalStateException){
            snack(ex.message ?: "Unknown error")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_next, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
