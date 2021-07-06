package com.example.xiaodongonline.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.xiaodongonline.R

/**
 *  author : 徐亚雄
 *  date : 2021/7/5 10:32
 *  description :
 */

object ImageUtil {

    // 添加水印
    fun addWatermask(
        context: Context,
        bitmap: Bitmap,
        reportNo: Int?,
        longitude: Float?,
        latitude: Float?,
        date: String
    ): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val txtSize = height / 30f
        val lineHeight = txtSize * 1f
        val marginTop = lineHeight / 3
        val marginBottom = marginTop * 1f
        val marginLeft = marginTop * 1f
        val marginRight = marginTop * 1f
        val bottomBase = height - marginBottom

        val paint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.blue_primary)
            textSize = txtSize
            isFakeBoldText = true
        }

        val dateStr = "检测时间：${date}"
        val dateStrWidth = paint.measureText(dateStr)
        val xOffset = width - dateStrWidth - marginRight

        canvas.drawText("核工业北京地质研究院", marginLeft, marginTop + lineHeight, paint)
        canvas.drawText("检测序号：${reportNo ?: "无"}", xOffset, bottomBase - lineHeight * 3, paint)
        canvas.drawText(dateStr, xOffset, bottomBase - lineHeight * 2, paint)
        canvas.drawText("经度：${longitude ?: "无"}", xOffset, bottomBase - lineHeight, paint)
        canvas.drawText("纬度：${latitude ?: "无"}", xOffset, bottomBase, paint)
        canvas.save()
        canvas.restore()
        return newBitmap
    }
}