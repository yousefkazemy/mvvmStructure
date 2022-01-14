package com.example.mvvmstructure.utils.screen

import android.content.res.Resources

object ScreenUtils {

    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels

    fun getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels

    private fun calculateScaleFactor(imageWidth: Int?) =
        imageWidth?.let {
            return@let getScreenWidth().toFloat() / it
        } ?: 1f

    fun calculatePlaceHolderHeight(imageWidth: Int?, imageHeight: Int?) =
        imageHeight?.let { height ->
            return@let (calculateScaleFactor(imageWidth) * height).toInt()
        } ?: getScreenHeight() / 3
}