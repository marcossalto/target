package com.marcossalto.targetmvd.util.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Using Glide to load an image
 * in case you change Glide by other library like Picasso or Fresco
 * just change this extension
 * @param uri [Uri] Image URI
 */
fun ImageView.load(uri: Uri) {
    Glide.with(context)
        .load(uri)
        .into(this)
}

/**
 * Add more extensions in case you need it
 */

fun ImageView.load(url: String) {
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.load(resourceId: Int) {
    Glide.with(context)
        .load(resourceId)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}
