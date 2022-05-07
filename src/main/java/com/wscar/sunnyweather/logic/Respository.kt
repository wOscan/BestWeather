package com.wscar.sunnyweather.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.wscar.sunnyweather.logic.dao.PlaceDao
import com.wscar.sunnyweather.logic.model.Weather
import com.wscar.sunnyweather.logic.network.SunnyWeatherNetwork
import com.wscar.sunnyweather.ui.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Dispatcher
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Respository {
//    fun searchPlaces(query:String) = liveData(Dispatchers.IO){
//        val result = try {
//            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
//            if (placeResponse.code == "200"){
//                val places = placeResponse.location
//
//                Result.success<List<Place>>(places)
//            }else{
//                Result.failure<RuntimeException>(RuntimeException("response status is ${placeResponse.code}"))
//            }
//        }catch (e:Exception){
//            Result.failure<Exception>(e)
//        }
//        emit(result)
//    }
    fun savePlace(place:Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaced()

    fun searchPlaces(query:String) = fire(Dispatchers.IO){

            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.code == "200"){
                val places = placeResponse.location

                Result.success<List<Place>>(places)
            }else{
                Result.failure<RuntimeException>(RuntimeException("response status is ${placeResponse.code}"))
            }


    }

    fun refreshWeather(location:String) = liveData (Dispatchers.IO){
        val result = try {
            coroutineScope {
                val deferredRealtime = async{
                    SunnyWeatherNetwork.getRealtimeWeather(location)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.
                            getDaliyWeather(location)
                }

                val realtimeResponse = deferredRealtime.await()
                val daliyResponse = deferredDaily.await()
                if (realtimeResponse.code == "200" && daliyResponse.code == "200"){
                    val weather = Weather(realtimeResponse.result,daliyResponse.daily)
                    Result.success(weather)
                }else{
                    Result.failure(RuntimeException("realtime response status is ${realtimeResponse.code} ,dailyResponse code is ${daliyResponse.code}"))
                }
            }
        }catch (e:Exception){
            Result.failure<Weather>(e)
        }
        emit(result)
    }

    private fun <T> fire(context:CoroutineContext,block:suspend() -> Result<T>) = liveData<Result<T>>(context){
        val result = try {
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}