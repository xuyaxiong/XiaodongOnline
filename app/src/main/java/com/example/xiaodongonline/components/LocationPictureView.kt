package com.example.xiaodongonline.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.xiaodongonline.R

class LocationPictureView : ConstraintLayout {
    private lateinit var label: TextView
    private lateinit var image: ImageView
    private lateinit var status: TextView
    var onClick: ((String) -> Unit)? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
        initAttrs(attrs)
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.location_picture_view, this, true)
        label = findViewById(R.id.label)
        image = findViewById(R.id.image)
        status = findViewById(R.id.uploadStatus)
        this.setOnClickListener {
            onClick?.invoke(label.text.toString())
        }
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LocationPictureView)
        val str = typedArray.getString(R.styleable.LocationPictureView_label)
        if (!TextUtils.isEmpty(str)) {
            label.text = str
        }
        typedArray.recycle()
    }

    fun getLabel(): String {
        return label.text.toString()
    }

    fun setLabel(label: String) {
        this.label.text = label
    }

    fun setBitmap(bitmap: Bitmap) {
        image.setImageBitmap(bitmap)
    }

    fun setUploadStatus(uploadStatus: UploadStatus) {
        status.text = uploadStatus.label
        when (uploadStatus) {
            UploadStatus.SUCCEED -> status.setBackgroundColor(Color.GREEN)
            UploadStatus.FAILED -> status.setBackgroundColor(Color.RED)
            UploadStatus.NOT_UPLOADED -> status.setBackgroundColor(Color.GRAY)
        }
        if (status.visibility == View.GONE) {
            status.visibility = View.VISIBLE
        }
    }

    enum class UploadStatus(val label: String) {
        SUCCEED("已上传"), FAILED("上传失败"), NOT_UPLOADED("未上传")
    }
}