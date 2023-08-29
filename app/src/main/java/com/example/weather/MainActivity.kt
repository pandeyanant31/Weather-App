package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.example.weather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//fd838b0ae501b929f35f6b5fa8fb912b
class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Lucknow")
        Searchcity()
    }

    private fun Searchcity() {
        val searchview = binding.searchView
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query !=null)
                {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(cityName, appid ="fd838b0ae501b929f35f6b5fa8fb912b", units = "metric" )
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody !=null){
                    val temperature = responseBody.main.temp
                    //Log.d("TAG", "onResponse: $temperature")
                    binding.temp.text="$temperature °C"
                    val maxtemper = responseBody.main.temp_max
                    binding.maxTemp.text ="Max : $maxtemper °C"

                    val mintemper = responseBody.main.temp_min
                    binding.minTemp.text="Min : $mintemper °C"

                    val humidities = responseBody.main.humidity
                    binding.Humidity.text="$humidities %"

                    val pres = responseBody.main.pressure
                    binding.sea.text = "$pres hPa"

                    val ws = responseBody.wind.speed
                    binding.windSpeed.text = "$ws m/s"

                    val sunr = responseBody.sys.sunrise.toLong()
                    val suns = responseBody.sys.sunset.toLong()

                    binding.sunRise.text = "${time(sunr)}"
                    binding.sunset.text = "${time(suns)}"

                    val condition = responseBody.weather.firstOrNull()?.main?: "unknown"

                    binding.weather.text = condition

                    binding.condition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                        binding.date.text= date()
                        binding.cityName.text="$cityName"

                    changeImagesAccordingToWeatherCondition(condition)

                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        }

    private fun changeImagesAccordingToWeatherCondition(condition:String) {
        when(condition){
            "Clear Sky", "Sunny", "Clear"-> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy"-> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain"-> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard"-> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp:Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

    fun dayName(timestamp:Long):String{
        // sdf = simple date format
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}