package com.youtube.firebaserealtime.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.youtube.firebaserealtime.R

fun ImageView.load(url: String, placeHolder: Int = R.drawable.placeholder_cat,
                   centerCrop: Boolean = false,
                   fitCenterCrop: Boolean = false,
                   circleCrop: Boolean = false,
                   decoder: DecodeFormat = DecodeFormat.PREFER_RGB_565) {
    val request = Glide.with(this.context)
            .load(url)
            .centerInside()
            .format(decoder)
            .placeholder(placeHolder)
    if(centerCrop) request.apply(RequestOptions.centerCropTransform())
    if(circleCrop) request.apply(RequestOptions.circleCropTransform())
    if(fitCenterCrop) request.apply(RequestOptions.fitCenterTransform())
    request.into(this)
}
