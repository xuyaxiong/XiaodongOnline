package com.example.xiaodongonline.utils

import android.content.Context
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery
import io.reactivex.Observable

interface ImagePicker {
    @Gallery
    fun openGallery(context: Context): Observable<Result>

    @Camera
    fun openCamera(context: Context): Observable<Result>
}