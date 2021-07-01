package com.example.xiaodongonline.dto

interface Mapper<T> {
    fun transform(): T
}