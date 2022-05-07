package com.wscar.sunnyweather.logic.network

import com.wscar.sunnyweather.MyApplication
import com.wscar.sunnyweather.logic.model.RealtimeResponse
import com.wscar.sunnyweather.logic.model.daliyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("v7/weather/now?key=${MyApplication.TOKEN}")
    fun getRealtimeWeather(@Query("location")  location:String):Call<RealtimeResponse>


    @GET("v7/weather/3d?key=${MyApplication.TOKEN}")
    fun getDailyWeather(@Query("location") location: String):Call<daliyResponse>
}