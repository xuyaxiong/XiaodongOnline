package com.example.xiaodongonline.model

import com.example.xiaodongonline.Config

data class Report(
    val orderNo: Int? = null,
    val reportNo: Int? = null,
    val cmccStationName: String = "", // 移动基站名称
    val cuccStationName: String = "", // 联通基站名称
    val ctccStationName: String = "", // 电信基站名称
    val cityNo: Int? = null, // 城市编号
    val detectionLocation: String = "", // 检测地点
    val operator: String = "", // 运营商
    val longitude: Float? = null, // 经度
    val latitude: Float? = null, // 纬度
    val type: String = "", // 铁塔种类
    val cmccDirectionAngle: Float? = null, // 移动方向角
    val cuccDirectionAngle: Float? = null, // 电信方向角
    val ctccDirectionAngle: Float? = null, // 联通方向角
    val antennaHeight: Float? = null, // 天线挂高
    val detectionDate: String = "", // 检测时间

    var weather: String = "", // 天气
    var temperature: String = "", // 温度
    var humidity: String = "", // 湿度
    var inspector: String = "", // 检测员
    var equipment: String = "", // 检测设备
    var date: String = "", // 检测时间
    var remark: String = "", // 备注
) {
    var pictureMap = mutableMapOf<String, String>()

    init {
        for (label in Config.POSITION_LABEL) {
            if (label !in pictureMap.keys) {
                pictureMap[label] = ""
            }
        }
    }
}