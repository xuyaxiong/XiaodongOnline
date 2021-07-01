package com.example.xiaodongonline.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.xiaodongonline.R
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


class ReportFragment : Fragment(R.layout.report_fragment) {

    companion object {
        fun newInstance() = ReportFragment()
    }

    private val viewModel: ReportViewModel by viewModels()
    private lateinit var binding: ReportFragmentBinding
    private var loading: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ReportFragmentBinding.bind(view)
        binding.report = viewModel.report
        binding.lifecycleOwner = this
        loading = ProgressBar(requireContext())
        loading?.visibility = View.VISIBLE
        viewModel.showLoading.observe(viewLifecycleOwner) {
        }

        val report = Report(reportNo = 1)
//        viewModel.report.value?.let { initPictures(it) }
        initPictures(report)

        // 查询
        viewModel.initQuery()
        binding.editTextSearch.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe {
                viewModel.emitQueryWord(it.toString())
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

    fun formatPictureName(reportNo: Int, label: String): String {
        return "NO${reportNo}_${label}"
    }
}