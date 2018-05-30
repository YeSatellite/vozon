package com.yesat.vozon.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * Created by yesat on 09/03/18.
 */

class Transport : Serializable {
    @Expose var id: Long? = null

    @Expose var owner: User? = null

    @Expose var type: Long? = null
    @SerializedName("type_name")
    @Expose var typeName: String? = null

    @Expose var model: Long? = null
    @SerializedName("model_name")
    @Expose var modelName: String? = null
    @SerializedName("mark_name")
    @Expose var markName: String? = null
    var mark: Long? = null

    @Expose var body: Long? = null
    @SerializedName("body_name")
    @Expose var bodyName: String? = null

    @SerializedName("shipping_type")
    @Expose var shippingType: Long? = null
    @SerializedName("shipping_type_name")
    @Expose var shippingTypeName: String? = null

    @Expose var image1: String? = null
    @Expose var image2: String? = null

    @Expose var number: String? = null

    @Expose var width: Float? = null

    @Expose var height: Float? = null

    @Expose var length: Float? = null

    @Expose var comment: String? = null

    val fullName: String get() = "$modelName, $markName"
}
