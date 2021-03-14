package com.alex.mediacenter.util

import android.content.res.Resources

fun Resources.getStatusBarHeight(): Int {
    return getIdentifier("status_bar_height", "dimen", "android")
            .let { resourceId ->
                when (resourceId > 0) {
                    true -> getDimensionPixelSize(resourceId)
                    false -> 0
                }
            }
}

fun Resources.getBottomBarHeight(): Int {
    return getIdentifier("navigation_bar_height", "dimen", "android")
            .let { resourceId ->
                when (resourceId > 0) {
                    true -> getDimensionPixelSize(resourceId)
                    false -> 0
                }
            }
}