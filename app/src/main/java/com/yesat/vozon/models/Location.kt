package com.yesat.vozon.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Location : Serializable {
    @Expose var id: Long? = null

    @Expose var name: String? = null

    @SerializedName("region_name")
    @Expose var regionName: String? = null

    @SerializedName("country_name")
    @Expose var countryName: String? = null

    fun getShortName(): String{
        return "$name, $countryName"
    }
    fun getShortName(startDetail: String): String{
        return "$name, $countryName, $startDetail"
    }
    fun getShortXName(startDetail: String): String{
        return "$name, $startDetail"
    }

    operator fun minus(other: Location): String{
        if (this.countryName != other.countryName) return "kasha"
        if (this.regionName != other.regionName) return "Same Country"
        if (this.name != other.name) return "Same Region"
        return "Same City"
    }
}
