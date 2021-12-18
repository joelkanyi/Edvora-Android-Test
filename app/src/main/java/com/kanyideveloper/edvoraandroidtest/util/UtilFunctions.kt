package com.kanyideveloper.edvoraandroidtest.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun formatDate(timestamp: String): String {
    val df1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val result = df1.parse(timestamp)
    val startCalendar = Calendar.getInstance()
    startCalendar.time = result
    val sdf = SimpleDateFormat("dd:MM:yyyy")
    return sdf.format(startCalendar.time)
}