package com.wscar.sunnyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.wscar.sunnyweather.MyApplication
import com.wscar.sunnyweather.ui.model.Place

object PlaceDao {
    fun savePlace(place:Place){
        sharePreferences().edit{
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace():Place{
        val placeJson = sharePreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }

    fun isPlaceSaced() = sharePreferences().contains("place")
    fun sharePreferences() = MyApplication.context.getSharedPreferences("location_data",Context.MODE_PRIVATE)
}