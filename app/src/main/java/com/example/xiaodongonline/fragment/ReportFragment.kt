package com.example.xiaodongonline.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.xiaodongonline.R
import com.example.xiaodongonline.components.LoadingView
import com.example.xiaodongonline.components.LocationPictureView
import com.example.xiaodongonline.databinding.ReportFragmentBinding
import com.example.xiaodongonline.model.Report
import com.example.xiaodongonline.utils.ImagePicker
import com.example.xiaodongonline.utils.ImageUtil
import com.example.xiaodongonline.utils.toast
import com.jakewharton.rxbinding4.widget.textChanges
import com.qingmei2.rximagepicker.core.RxImagePicker
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *  author : 徐亚雄
 *  date : 2021/7/5 10:35
 *  description :
 */


class ReportFragment : Fragment(R.layout.report_fragment) {

    companion object {
        fun newInstance() = ReportFragment()
    }

    private val viewModel: ReportViewModel by viewModels()
    private lateinit var binding: ReportFragmentBinding
    private lateinit var loadingView: LoadingView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ReportFragmentBinding.bind(view)
        binding.report = viewModel.report
        binding.lifecycleOwner = this
        loadingView = LoadingView(requireContext())
        viewModel.showLoading.observe(viewLifecycleOwner) { show ->
            if (show) {
                loadingView.show()
            } else {
                loadingView.hide()
            }
        }

        val report = Report(reportNo = 1)
//        viewModel.report.value?.let { initPictures(it) }
        initPictures(report)

        // 查询
        viewModel.initQuery()
        binding.editTextSearch.textChanges()
            .skip(1)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe {
                viewModel.emitReportNo(it.toString())
            }

        // 测试按钮
        binding.button.setOnClickListener {
        }
    }

    private fun initPictures(report: Report) {
        val pictureMap = report.pictureMap
        var account = 0
        var linearLayout: LinearLayout? = null
        var rowLayoutParams: LinearLayout.LayoutParams? = null
        for (label in pictureMap.keys) {
            if (account % 2 == 0) {
                linearLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                    weightSum = 2f
                }
                rowLayoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                rowLayoutParams.setMargins(0, 15, 0, 0)
            }
            val locationPictureView = LocationPictureView(requireContext()).apply {
                setLabel(label)
                onClick = {
                    takePhoto(this, report)
                }
                layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }
            locationPictureView.startAnim()
            Glide.with(requireContext())
                .load("https://cdn.pixabay.com/photo/2021/02/27/22/19/plant-6055943_1280.jpg")
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        locationPictureView.stopAnim()
                        return false
                    }
                })
                .into(locationPictureView.image)

            linearLayout?.addView(locationPictureView)
            if (account % 2 == 1) {
                binding.imageContainer.addView(linearLayout, rowLayoutParams)
                linearLayout = null
            }
            account += 1
        }
        if (linearLayout != null) {
            binding.imageContainer.addView(linearLayout, rowLayoutParams)
        }
    }


    @SuppressLint("CheckResult")
    private fun takePhoto(locationPictureView: LocationPictureView, report: Report) {
        RxImagePicker.create(ImagePicker::class.java)
            .openCamera(requireActivity())
            .subscribe {
                val uri = it.uri
                Glide.with(requireActivity()).asBitmap().load(uri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val newBitmap =
                                ImageUtil.addWatermask(
                                    requireContext(),
                                    resource,
                                    report.reportNo,
                                    report.longitude,
                                    report.latitude,
                                    report.detectionDate
                                )
                            locationPictureView.setBitmap(newBitmap)
                            val pictureName =
                                formatPictureName(report.reportNo!!, locationPictureView.getLabel())
                            toast(pictureName)
                            locationPictureView.setUploadStatus(LocationPictureView.UploadStatus.SUCCEED)
                            // TODO("上传图片")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                    })
            }
    }

    // 上传图片命名
    fun formatPictureName(reportNo: Int, label: String): String {
        return "NO${reportNo}_${label}"
    }
}