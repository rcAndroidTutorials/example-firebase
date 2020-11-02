package com.youtube.firebaserealtime.extensions

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

fun View.snack(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}
//FragmentActivity is the parent of AppCpmpatActivity
fun FragmentActivity.snack(message: Int) {
    findViewById<View>(android.R.id.content).snack(message)
}