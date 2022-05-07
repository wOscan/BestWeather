package com.wscar.sunnyweather.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wscar.sunnyweather.MainActivity
import com.wscar.sunnyweather.R
import com.wscar.sunnyweather.ui.model.Place
import com.wscar.sunnyweather.ui.weather.WeatherActivity

class PlaceAdapter (private val fragment: PlaceFragment,private val placeList: List<Place>):RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder = ViewHolder(view)

        // 开始绑定事件
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            val place = placeList[position]

            // 判断是否在weatherActivity中，如果不在，那么填入数据，开启新的Intent，否则关闭侧边栏，刷新天气情况
            val activity = fragment.activity
            if ( activity is MainActivity){
                val intent = Intent(parent.context,WeatherActivity::class.java).apply {
                    putExtra(WeatherActivity.LOCATION,place.id)
                    putExtra(WeatherActivity.PLACE_NAME,place.name)
                }
//                Log.d("onCreateViewHolder","if "+activity.toString())
                fragment.startActivity(intent)
                fragment.activity?.finish()
            }else{
//                Log.d("onCreateViewHolder",activity.toString())

                (activity as WeatherActivity).drawerlayout.closeDrawers()
                (activity as WeatherActivity).refreshWeather(place.id)
                (activity as WeatherActivity).viewModel.placeName = place.name
            }

            // 不管是去启动新的，还是在选择，都要把选择的数据保留
            fragment.viewModel.savePlace(place)


        }
        return holder
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.country+" "+place.adm1+" "+place.adm2+" "+place.name
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val placeName = view.findViewById<TextView>(R.id.placeName)
        val placeAddress = view.findViewById<TextView>(R.id.placeAddress)
    }
}