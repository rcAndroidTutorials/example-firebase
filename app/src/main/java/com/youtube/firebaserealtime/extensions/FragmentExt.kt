package com.youtube.firebaserealtime.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import com.youtube.firebaserealtime.R

fun Fragment.slideAnimation(): NavOptions.Builder {
    return androidx.navigation.NavOptions.Builder()
        .setEnterAnim(R.anim.enter)
        .setExitAnim(R.anim.exit)
        .setPopEnterAnim(R.anim.pop_enter)
        .setPopExitAnim(R.anim.pop_exit)
}
fun Fragment.popAnimation(@IdRes fragmentId: Int): NavOptions.Builder {
    return slideAnimation()
        .setPopUpTo(fragmentId, true)
}