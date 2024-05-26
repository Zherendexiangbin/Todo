package net.onest.time.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import net.onest.time.application.TimeApplication

fun String.makeToast(duration: Int = Toast.LENGTH_SHORT): Toast = Toast.makeText(
    applicationContext(),
    this,
    duration
)

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(
        applicationContext(),
        this,
        duration
    ).show()
}

fun String.showSnackBar(view: View, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        view,
        this,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun applicationContext(): Context = TimeApplication.application!!.applicationContext