package com.example.experiment04

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var etCity: EditText
    private lateinit var btnGetWeather: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvCondition: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWind: TextView

    // Put your OpenWeather API key here
    private val API_KEY = "YOUR_API_KEY_HERE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCity = findViewById(R.id.etCity)
        btnGetWeather = findViewById(R.id.btnGetWeather)
        progressBar = findViewById(R.id.progressBar)

        tvCity = findViewById(R.id.tvCity)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvCondition = findViewById(R.id.tvCondition)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvWind = findViewById(R.id.tvWind)

        btnGetWeather.setOnClickListener {

            val city = etCity.text.toString().trim()

            if (city.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter a city name",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                getWeather(city)
            }
        }
    }

    private fun getWeather(city: String) {

        progressBar.visibility = View.VISIBLE
        btnGetWeather.isEnabled = false

        Thread {

            try {

                val encodedCity = URLEncoder.encode(city, "UTF-8")

                val urlString =
                    "https://api.openweathermap.org/data/2.5/weather" +
                            "?q=$encodedCity" +
                            "&appid=$API_KEY" +
                            "&units=metric"

                val url = URL(urlString)

                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    val response = connection.inputStream
                        .bufferedReader()
                        .use { it.readText() }

                    val jsonObject = JSONObject(response)

                    val cityName = jsonObject.getString("name")

                    val mainObject = jsonObject.getJSONObject("main")

                    val temperature =
                        mainObject.getDouble("temp")

                    val humidity =
                        mainObject.getInt("humidity")

                    val weatherArray =
                        jsonObject.getJSONArray("weather")

                    val weatherObject =
                        weatherArray.getJSONObject(0)

                    val condition =
                        weatherObject.getString("description")

                    val windObject =
                        jsonObject.getJSONObject("wind")

                    val windSpeed =
                        windObject.getDouble("speed")

                    runOnUiThread {

                        tvCity.text = cityName

                        tvTemperature.text =
                            "Temperature: %.1f °C".format(temperature)

                        tvCondition.text =
                            "Condition: ${condition.replaceFirstChar { it.uppercase() }}"

                        tvHumidity.text =
                            "Humidity: $humidity %"

                        tvWind.text =
                            "Wind Speed: $windSpeed m/s"

                        progressBar.visibility = View.GONE
                        btnGetWeather.isEnabled = true
                    }

                } else {

                    runOnUiThread {

                        progressBar.visibility = View.GONE
                        btnGetWeather.isEnabled = true

                        when (responseCode) {

                            401 -> Toast.makeText(
                                this,
                                "Invalid or inactive API key",
                                Toast.LENGTH_LONG
                            ).show()

                            404 -> Toast.makeText(
                                this,
                                "City not found",
                                Toast.LENGTH_LONG
                            ).show()

                            else -> Toast.makeText(
                                this,
                                "Server error: $responseCode",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                connection.disconnect()

            } catch (e: Exception) {

                runOnUiThread {

                    progressBar.visibility = View.GONE
                    btnGetWeather.isEnabled = true

                    Toast.makeText(
                        this,
                        "Network error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }.start()
    }
}