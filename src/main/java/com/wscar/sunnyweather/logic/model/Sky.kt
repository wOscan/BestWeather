package com.wscar.sunnyweather.logic.model

import com.wscar.sunnyweather.R

class Sky (val icon:Int,val bg:Int)

private val sky = mapOf(
    "100" to Sky(R.drawable.ic_clear_day,R.drawable.bg_clear_day)
)
fun getKey(code:String):Sky =  sky[code]?:sky["100"]!!
