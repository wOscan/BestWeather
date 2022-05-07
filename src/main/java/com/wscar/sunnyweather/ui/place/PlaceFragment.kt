package com.wscar.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wscar.sunnyweather.MainActivity
import com.wscar.sunnyweather.MyApplication
import com.wscar.sunnyweather.R
import com.wscar.sunnyweather.logic.model.Weather
import com.wscar.sunnyweather.ui.model.Place
import com.wscar.sunnyweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment :Fragment(){
    lateinit var recyclerView: RecyclerView
    lateinit var searchPlaceEdit1:EditText
    lateinit var toastInPlace:Toast

    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    lateinit var adapter: PlaceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_place,container,false)
        recyclerView = view.findViewById(R.id.recycler)
        searchPlaceEdit1 = view.findViewById<EditText>(R.id.searchPlaceEdit)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 判断用户选择的数据是否保存,以及是否在weatherActivity中，如果保存且不在weatherActivity中，就读取数据，直接跳转,
        if (viewModel.isPlaceSaved()&&activity is MainActivity){
            val place = viewModel.getSavedPlace()
            val intent = Intent(context,WeatherActivity::class.java).apply {
                putExtra(WeatherActivity.PLACE_NAME,place.name)
                putExtra(WeatherActivity.LOCATION,place.id)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        toastInPlace = Toast.makeText(activity,"未能查询到地点",Toast.LENGTH_SHORT)

        val layoutManager  = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        adapter = PlaceAdapter(this,viewModel.placeList)
        recyclerView.adapter = adapter
        searchPlaceEdit1.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val content = p0.toString()
                if (content.isNotEmpty()){
                    viewModel.searchPlaces(content)
                }else{
                    recyclerView.visibility = View.GONE
                    bgImageView.visibility = View.VISIBLE
                    viewModel.placeList.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }


        })

        // 给城市返回值设置回调事件，如果数据获取到了，就把他显示在界面上，如果没有弹出对话框，提示用户
        viewModel.placeLiveData.observe(viewLifecycleOwner,Observer {
            var places = it.getOrNull()

            if (places != null){

                // 如果获取到了数据，就把正在显示，或者还么有显示的弹框取消
                toastInPlace?.cancel()

                places = places as List<Place>
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{

                toastInPlace.show()
            }
        })

    }


}