package com.example.xiaodongonline.services

import com.example.xiaodongonline.dto.ReportDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET


interface ReportService {
    @GET("test")
    fun getReport(): Deferred<ReportDTO>
}