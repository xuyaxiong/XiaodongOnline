package com.example.xiaodongonline.dto

import com.example.xiaodongonline.model.Report

class ReportDTO() : Mapper<Report> {
    override fun transform(): Report {
        return Report()
    }

}
