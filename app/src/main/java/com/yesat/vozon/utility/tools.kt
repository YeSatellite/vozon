package com.yesat.vozon.utility

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.yesat.vozon.models.Location
import com.yesat.vozon.models.User
import com.yesat.vozon.ui.client.XMainActivity
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import java.io.File
import java.io.FileOutputStream


/*
Log
 */
private const val tag_prefix = "xxx"
private const val tag_norm = "$tag_prefix<->norm"
private const val tag_hana = "$tag_prefix<->hana"

fun Any.norm(text: String?,tr : Throwable? = null){
    if (tr == null) Log.d(tag_norm,"${this::class.java.simpleName}: $text")
    else Log.d(tag_norm,"${this::class.java.simpleName}: $text",tr)
}



fun AppCompatActivity.addBackPress(){
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    supportActionBar!!.setDisplayShowHomeEnabled(true)
}

fun Activity.snack(text: String){
    val rootView = window.decorView.findViewById<View>(android.R.id.content)
    Snackbar.make(rootView,text, Snackbar.LENGTH_LONG).show()
}

fun View.snack(text: String){
    Snackbar.make(this,text, Snackbar.LENGTH_LONG).show()
}



fun EditText.get(error: String = ""): String{
    val text = this.text.toString().trim()
    if (error.isNotBlank() && text.isBlank())
        throw error(error)
    return text
}

var EditText.content : String
    get() = text.toString()
    set(value) {
        setText(value, TextView.BufferType.EDITABLE)
    }

var ImageView.src : String?
    get() = ""
    set(value){
        Picasso.get().load(value).into(this)
    }


fun SwipeRefreshLayout.setOnRefreshListenerAuto(listener: () -> Unit){
    setOnRefreshListener(listener)
    post({
        listener()
        isRefreshing = true
    })
    setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE)
}


@Suppress("UNCHECKED_CAST")
fun <T: Serializable> Intent.get(type : Class<T>): T{
    norm(">>>get "+type.simpleName)
    return this.getSerializableExtra(type.simpleName) as T
}

fun Intent.put(data: Serializable){
    norm(">>>put "+data::class.java.simpleName)
    this.putExtra(data::class.java.simpleName,data)
}

fun Activity.compressImage(uri: Uri): File {
    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

    val file = File(cacheDir,  "${System.currentTimeMillis()}.jpg")
    val os = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)
    os.flush()
    os.close()

    return file
}






fun locationFormat(location: Location, detail: String? = null): String{
    var str = "${location.countryName}, ${location.regionName}, ${location.name}"
    if (!detail.isNullOrBlank()) str += ", $detail"
    return str
}

fun clientOrCourier() : Class<*>? = when (Shared.currentUser.type){
    User.CLIENT -> XMainActivity::class.java
    User.COURIER -> XMainActivity::class.java
    else -> {
        Shared.currentUser = User()
        null
    }
}

fun String.dateFormat(): String {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    return SimpleDateFormat("d, MMMM", Locale.getDefault()).format(date)
}

fun setDateListener(activity: Activity) = View.OnClickListener {view ->
    val calendar = Calendar.getInstance()
    DatePickerDialog(activity,
            DatePickerDialog.OnDateSetListener {
                _, year, month, dayOfMonth ->
                (view as EditText).content = "$year-${month+1}-$dayOfMonth"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
}

fun setTimeListener(activity: Activity) = View.OnClickListener {view ->
    TimePickerDialog(activity,
            TimePickerDialog.OnTimeSetListener {
                _, hourOfDay, minute ->
                (view as EditText).content = "$hourOfDay:$minute"
            }, 0, 0, true).show()
}

fun Activity.askPermission(permission: String,requestCode: Int){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }
}



