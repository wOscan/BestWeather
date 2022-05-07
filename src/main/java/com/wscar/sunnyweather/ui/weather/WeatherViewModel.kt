package com.wscar.sunnyweather.ui.weather

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wscar.sunnyweather.logic.Respository

class WeatherViewModel:ViewModel(){
    private val locationLiveData = MutableLiveData<String>()

    var location = ""
    var placeName:String = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData){
        Respository.refreshWeather(it)
    }
    fun refreshWeather(location:String){
        locationLiveData.value = location
    }
}