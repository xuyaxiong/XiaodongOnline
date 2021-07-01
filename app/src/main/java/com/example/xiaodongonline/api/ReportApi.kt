package com.example.xiaodongonline.api

import com.example.xiaodongonline.services.ReportService
import com.example.xiaodongonline.utils.ApiUtil

object ReportApi {
    val reportService: ReportService by lazy {
        ApiUtil.retrofit.create(ReportService::class.java)
    }
}