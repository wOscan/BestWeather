package com.wscar.sunnyweather.ui.weather

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.getSystemService
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wscar.sunnyweather.R
import com.wscar.sunnyweather.logic.model.Sky
import com.wscar.sunnyweather.logic.model.Weather
import com.wscar.sunnyweather.logic.model.getKey
import kotlinx.android.synthetic.main.forecast_item.*

class WeatherActivity : AppCompatActivity() {

    companion object {
        const val LOCATION = "0xd243aa"
        const val PLACE_NAME = "0xacb23"
    }
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    lateinit var placeName:TextView
    lateinit var currentTemp:TextView
    lateinit var currentSky:TextView
    lateinit var currentPreesure:TextView
    lateinit var nowLayout: RelativeLayout
    lateinit var forecastLayout:LinearLayout
    lateinit var navBtn:ImageView
    lateinit var drawerlayout:DrawerLayout

    // 生活区
    lateinit var lifetext11:TextView
    lateinit var lifetext1:TextView
    lateinit var life_pic1:ImageView
    lateinit var lifetext22:TextView
    lateinit var lifetext2:TextView
    lateinit var life_pic2:ImageView
    lateinit var lifetext33:TextView
    lateinit var lifetext3:TextView
    lateinit var life_pic3:ImageView
    lateinit var lifetext44:TextView
    lateinit var lifetext4:TextView
    lateinit var life_pic4:ImageView

    lateinit var swiperefreshLayout:SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 隐藏状态栏
        val decorView = window.decorView
//        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.statusBarColor = Color.TRANSPARENT

//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_weather)

        // 获取传递过来的城市名 坐标
        if(viewModel.location.isEmpty()){
            viewModel.location = intent.getStringExtra(LOCATION)?:""
        }
        if(viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra(PLACE_NAME)?:""
        }

        // 寻找控件
        placeName = findViewById(R.id.placeName)
        currentTemp = findViewById(R.id.currentTemp)
        currentPreesure = findViewById(R.id.currentPreesure)
        currentSky = findViewById(R.id.currentSky)
        nowLayout = findViewById(R.id.nowLayout)
        forecastLayout = findViewById(R.id.forecastLayout)
        swiperefreshLayout = findViewById(R.id.swiperefreshLayout)
        navBtn = findViewById(R.id.navBtn)
        drawerlayout = findViewById(R.id.drawerlayout)

        lifetext11 = findViewById(R.id.life_text11)
        lifetext1 = findViewById(R.id.life_text1)
        life_pic1 = findViewById(R.id.life_pic1)
        lifetext22 = findViewById(R.id.life_text22)
        lifetext2 = findViewById(R.id.life_text2)
        life_pic2 = findViewById(R.id.life_pic2)
        lifetext33 = findViewById(R.id.life_text33)
        lifetext3 = findViewById(R.id.life_text3)
        life_pic3 = findViewById(R.id.life_pic3)
        lifetext44 = findViewById(R.id.life_text44)
        lifetext4 = findViewById(R.id.life_text4)
        life_pic4 = findViewById(R.id.life_pic4)

        // 绑定 weatherLiveData 监控
        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if(weather != null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this,"无法获取天气信息",Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
            swiperefreshLayout.isRefreshing = false
        })

        refreshWeather(viewModel.location)
        // 设置刷新控件的颜色
        swiperefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        // 当被下拉时 ，调用刷新
        swiperefreshLayout.setOnRefreshListener {
            refreshWeather(viewModel.location)
        }

        // 当点击按钮时，展开侧边栏
        navBtn.setOnClickListener{
            drawerlayout.openDrawer(GravityCompat.START)
        }

        // 设置导航栏点击事件,及时关闭输入法
        drawerlayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerOpened(drawerView: View) {
            }

        })

    }

    // 刷新天气
    fun refreshWeather(location:String){
        // 传入名字
        viewModel.refreshWeather(location)
        swiperefreshLayout.isRefreshing = true
    }
    fun showWeatherInfo(weather:Weather){
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        // 填充now 数据
        val  currentTempText = "${realtime.temp} ℃"
        currentTemp.text = currentTempText
        currentSky.text = realtime.text
        currentPreesure.text = "大气压 ${ realtime.pressure.toInt()/10} kPa"
        nowLayout.setBackgroundResource(getKey(realtime.icon).bg)

        // 填充forecast 数据
        forecastLayout.removeAllViews()
        for (day in  daily){
            val view  = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)
            val text = day.textDay
            val date = day.fxDate
            val skyIcon = getKey(day.iconDay)
            val temp = "${day.tempMin}-${day.tempMax} ℃"

            val tempView = view.findViewById<TextView>(R.id.tempratureInfo)
            val dataInfoView = view.findViewById<TextView>(R.id.dataInfo)
            val skyInfoView= view.findViewById<TextView>(R.id.skyInfo)
            val skyIconView = view.findViewById<ImageView>(R.id.skyIcon)

            tempView.text = text
            dataInfoView.text = date
            skyIconView.setImageResource(skyIcon.icon)
            skyInfoView.text = text
            forecastLayout.addView(view)
        }

        // 填充life_index 数据
            val windDir:String = realtime.windDir
        val windSpeed:String = realtime.windSpeed
        val precip:String = realtime.precip
        val vis:String = realtime.vis

        lifetext11.text = "风向"
        lifetext1.text = windDir

        lifetext22.text = "风速"
        lifetext2.text = windSpeed + " km/h"

        lifetext33.text = "降雨量"
        lifetext3.text = precip + " mm"

        lifetext44.text = "能见度"
        lifetext4.text = vis+" km"

    }
}
