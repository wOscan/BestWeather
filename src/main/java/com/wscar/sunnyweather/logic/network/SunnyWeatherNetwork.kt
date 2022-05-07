package com.wscar.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create1<WeatherService>()

    suspend fun searchPlaces(query:String) = placeService.searchPlaces(query).await()
    suspend fun getDaliyWeather(location:String) = weatherService.getDailyWeather(location).await()
    suspend fun getRealtimeWeather(location:String) = weatherService.getRealtimeWeather(location).await()


    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {
            enqueue(object :Callback<T>{
                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body!=null) it.resume(body)
                    else it.resumeWithException(RuntimeException("response body null"))
                }

            })
        }
    }
}