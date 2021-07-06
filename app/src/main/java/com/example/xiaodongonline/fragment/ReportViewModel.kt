package com.example.xiaodongonline.fragment

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xiaodongonline.model.Report
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class ReportViewModel : ViewModel() {
    private var _report = MutableLiveData<Report>()
    var report: LiveData<Report> = _report
    private val queryObservable = PublishSubject.create<String>()
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean> = _showLoading

    fun emitReportNo(reportNo: String) {
        queryObservable.onNext(reportNo)
    }

    @SuppressLint("CheckResult")
    fun initQuery() {
        queryObservable.debounce(500, TimeUnit.MILLISECONDS)
            .filter { !TextUtils.isEmpty(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { reportNo ->
                queryReport(reportNo.toInt())
            }
    }

    private fun queryReport(reportNo: Int) {
        _showLoading.value = true
        coroutineScope.launch {
//            val deferred = ReportApi.reportService.getReportAsync()
            try {
//                val report = deferred.await().transform()
                delay(2000)
                val testReport = Report(reportNo = reportNo)
                _report.value = testReport
            } catch (e: Exception) {
                Logger.e(e, "获取报告失败")
            } finally {
                _showLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}