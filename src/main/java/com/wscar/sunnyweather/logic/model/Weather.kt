package com.wscar.sunnyweather.logic.model

data class Weather(val realtime:RealtimeResponse.Today,val daily:List<daliyResponse.Daily>)