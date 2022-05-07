package com.wscar.sunnyweather.logic.network

import com.wscar.sunnyweather.MyApplication
import com.wscar.sunnyweather.ui.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/city/lookup?key=${MyApplication.TOKEN}")
    fun searchPlaces(@Query("location") query:String): Call<PlaceResponse>
}