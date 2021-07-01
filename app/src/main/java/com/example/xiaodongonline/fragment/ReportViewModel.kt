package com.example.xiaodongonline.fragment

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xiaodongonline.api.ReportApi
import com.example.xiaodongonline.model.Report
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ReportViewModel : ViewModel() {
    private var _report = MutableLiveData<Report>()
    var report: LiveData<Report> = _report
    private val queryObservable = PublishSubject.create<String>()
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _showLoading = MutableLiveData<Boolean>(false)
    val showLoading: LiveData<Boolean> = _showLoading

    fun emitQueryWord(keyword: String) {
        queryObservable.onNext(keyword)
    }

    @SuppressLint("CheckResult")
    fun initQuery() {
        queryObservable.debounce(500, TimeUnit.MILLISECONDS)
            .map { keyword -> queryReportFake(keyword) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { report ->
                _report.value = report
            }
    }

    private fun queryReportFake(keyword: String): Report {
        val report = Report(
            1, 1,
            keyword,
            "联通",
            "电信",
            1, "福建", "运营商",
            30f, 30f,
            "娱乐", 30f, 30f, 30f, 2f,
            "2020年11月04日"
        )
        return report
    }

    private fun queryReport(keyword: String) {
        coroutineScope.launch {
            val report = ReportApi.reportService.getReport()
            try {
                val data = report.await().transform()
                _report.value = data
            } catch (e: Exception) {
                Logger.e(e, "获取报告失败")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}