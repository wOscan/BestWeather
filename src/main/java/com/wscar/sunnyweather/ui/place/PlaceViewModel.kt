package com.wscar.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wscar.sunnyweather.logic.Respository
import com.wscar.sunnyweather.ui.model.Place

class PlaceViewModel :ViewModel(){
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData){
        Respository.searchPlaces(it)
    }

    fun searchPlaces(query:String){
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Respository.savePlace(place)
    fun getSavedPlace() = Respository.getSavedPlace()
    fun isPlaceSaved() = Respository.isPlaceSaved()

}