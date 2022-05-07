package com.wscar.sunnyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://geoapi.qweather.com/"
    private const val BASE_URL1 = "https://devapi.qweather.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val retrofit1 = Retrofit.Builder()
        .baseUrl(BASE_URL1)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun <T> create(serviceClass:Class<T>):T = retrofit.create(serviceClass)

    inline fun <reified T> create():T = create(T::class.java)
//     inline fun <reified T> create1():T = create(T::class.java)

    fun <T> create1(serviceClass:Class<T>):T = retrofit1.create(serviceClass)
    inline fun <reified  T> create1():T = create1(T::class.java)
}