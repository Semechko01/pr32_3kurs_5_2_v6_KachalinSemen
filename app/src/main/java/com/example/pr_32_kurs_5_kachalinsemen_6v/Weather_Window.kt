package com.example.pr_32_kurs_5_kachalinsemen_6v

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pr_32_kurs_5_kachalinsemen_6v.Data.DataBase
import com.example.pr_32_kurs_5_kachalinsemen_6v.Data.Weater_Helper
import com.example.pr_32_kurs_5_kachalinsemen_6v.Data.Weater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Weather_Window : AppCompatActivity() {
    private lateinit var text1 : EditText
    private lateinit var text2 : EditText
    private lateinit var text3 : EditText
    private lateinit var text4 : EditText
    private lateinit var text5 : EditText
    private lateinit var text_new : EditText
    private lateinit var button : AppCompatButton
    private lateinit var button2 : AppCompatButton
    private lateinit var dao: Weater_Helper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_window)
        text1 = findViewById<EditText>(R.id.text1)
        text2 = findViewById<EditText>(R.id.text2)
        text3 = findViewById<EditText>(R.id.text3)
        text4 = findViewById<EditText>(R.id.text4)
        text5 = findViewById<EditText>(R.id.text5)
        text_new = findViewById<EditText>(R.id.text_new)
        button = findViewById<AppCompatButton>(R.id.ButtonSearch)
        button2 = findViewById<AppCompatButton>(R.id.ButtonSearch2)
        dao = DataBase.getInstance(this).weater_Helper()
        button.setOnClickListener{
            if(text_new.text.toString().length == 0 || text_new.text.toString() == null)
            {
                showAlert("Пустая строка")
                return@setOnClickListener
            }
            var key = "29fe78a50724443b9dd84159260910"
            var url = "https://api.weatherapi.com/v1/current.json?key=$key&q=${text_new.text.toString()}&lang=ru"
            val queue = Volley.newRequestQueue(this)
            Log.d("MyLog", "Temp22222")
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    Log.d("MyLog", "Temp222221")
                    val obj = JSONObject(response)
                    val current = obj.getJSONObject("current")

                    val temp = current.getDouble("temp_c")
                    val pressure = current.getDouble("pressure_mb")
                    val wind = current.getDouble("wind_kph")
                    val cloud = current.getInt("cloud")
                    text1.setText(text_new.text.toString())

                    text2.setText("$temp")
                    text3.setText("$pressure")
                    text4.setText("$wind")
                    text5.setText("$cloud")
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO)
                        {
                            dao.insert(Weater(name = text_new.text.toString(), temp = temp,pressure = pressure,windSpeed = wind,cloud = cloud))
                        }
                    }

                    Log.d("MyLog", "Current Download")

                },
                {
                    Log.d("MyLog", "Volley Error: $it")
                }
            )
            queue.add(stringRequest)
        }
        button2.setOnClickListener{

        }
    }
    fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}