package net.onest.time.utils

import android.widget.Toast
import net.onest.time.application.TimeApplication

fun String.showToast(duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(
        TimeApplication.application!!.applicationContext,
        this,
        duration
    ).show()
}