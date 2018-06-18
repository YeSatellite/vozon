package com.yesat.vozon.utility

import android.app.Activity
import com.yesat.vozon.R

fun Activity.yesOrNo(boolean: Boolean?): String {
    return if (boolean == true) getString(R.string.yes)
    else getString(R.string.no)
}