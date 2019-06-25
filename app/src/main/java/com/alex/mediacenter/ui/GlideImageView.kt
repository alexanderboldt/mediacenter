package com.alex.mediacenter.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import jp.wasabeef.glide.transformations.BlurTransformation

class GlideImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ImageView(context, attrs) {

    fun setImage(url: String?, blur: Boolean = false) {
        var transformations = arrayOf<Transformation<Bitmap>>()
        if (blur) transformations = transformations.plus(BlurTransformation(25, 20))
        transformations = transformations.plus(CenterCrop())

        GlideApp.with(context)
            .load(url)
            .transform(*transformations)
            .into(this)
    }
}