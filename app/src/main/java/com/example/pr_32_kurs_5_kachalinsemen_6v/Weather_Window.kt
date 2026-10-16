package com.example.pr_32_kurs_5_kachalinsemen_6v

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var text6 : EditText
    private lateinit var text_new : EditText
    private lateinit var button : AppCompatButton
    private lateinit var button2 : AppCompatButton
    private lateinit var button3 : AppCompatButton
    private lateinit var button4 : AppCompatButton
    private lateinit var button5 : AppCompatButton
    private lateinit var button6 : AppCompatButton
    private lateinit var dao: Weater_Helper
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_window)
        prefs = getSharedPreferences("user2", Context.MODE_PRIVATE)
        text1 = findViewById<EditText>(R.id.text1)
        text2 = findViewById<EditText>(R.id.text2)
        text3 = findViewById<EditText>(R.id.text3)
        text4 = findViewById<EditText>(R.id.text4)
        text5 = findViewById<EditText>(R.id.text5)
        text6 = findViewById<EditText>(R.id.text6)
        text_new = findViewById<EditText>(R.id.text_new)
        button = findViewById<AppCompatButton>(R.id.ButtonSearch)
        button2 = findViewById<AppCompatButton>(R.id.ButtonSearch2)
        button3 = findViewById<AppCompatButton>(R.id.ButtonSearch3)
        button4 = findViewById<AppCompatButton>(R.id.ButtonSearch4)
        button5 = findViewById<AppCompatButton>(R.id.ButtonSearch5)
        button6 = findViewById<AppCompatButton>(R.id.ButtonBack)
        dao = DataBase.getInstance(this).weater_Helper()
        button.setOnClickListener{
            if(text_new.text.toString().length == 0 || text_new.text.toString() == null)
            {
                showAlert("Пустая строка","Ошибка")
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
            lifecycleScope.launch {
                val list = withContext(Dispatchers.IO) { dao.getAll() }

                if (list.isEmpty()) {
                    showAlert("Нет сохранённых записей","Ошибка")
                    return@launch
                }
                val message = list.joinToString("\n\n") { w ->
                    "ID: ${w.id}\n" +
                    "Город: ${w.name}\n" +
                    "Температура: ${w.temp}°C\n" +
                    "Давление: ${w.pressure} мм рт.ст.\n" +
                    "Ветер: ${w.windSpeed} км/ч\n" +
                    "Облачность: ${w.cloud}%"
                }
                showAlert(message,"Сохраненые данные");
            }
        }
        button3.setOnClickListener{
            if(text6.text.toString().length == 0)
            {
                showAlert("Пустая строка с id","Ошибка")
                return@setOnClickListener
            }
            lifecycleScope.launch {
                var obj = withContext(Dispatchers.IO) { dao.getById(text6.text.toString().toLong()) }
                if(obj == null) {
                    showAlert("Таких данных нету","Ошибка")
                    return@launch
                }
                text1.setText("${obj.name}")
                text2.setText("${obj.temp}")
                text3.setText("${obj.pressure}")
                text4.setText("${obj.windSpeed}")
                text5.setText("${obj.cloud}")
                showAlert("Данные под ID: ${text6.text.toString().toLong()} теперь вы можете редактировать","Отлично")
            }
        }
        button4.setOnClickListener{
            if(text6.text.toString().length == 0)
            {
                showAlert("Пустая строка с id","Ошибка")
                return@setOnClickListener
            }
            lifecycleScope.launch {
                var obj = withContext(Dispatchers.IO) { dao.getById(text6.text.toString().toLong()) }
                if(obj == null) {
                    showAlert("Таких данных нету","Ошибка")
                    return@launch
                }
                withContext(Dispatchers.IO) { dao.delete(obj) }
                showAlert("Данные под ID: ${text6.text.toString().toLong()} удалены","Отлично")
            }
        }
        button5.setOnClickListener{
            if(text6.text.toString().length == 0)
            {
                showAlert("Пустая строка с id","Ошибка")
                return@setOnClickListener
            }
            lifecycleScope.launch {
                var obj = withContext(Dispatchers.IO) { dao.getById(text6.text.toString().toLong()) }
                if(obj == null) {
                    showAlert("Таких данных нету","Ошибка")
                    return@launch
                }
                if(text_new.text.toString().length == 0 || text2.text.toString().length == 0 || text3.text.toString().length == 0 || text4.text.toString().length == 0 || text5.text.toString().length == 0)
                {
                    showAlert("Пустые строки","Ошибка")
                    return@launch
                }
                var s = Weater(id = obj.id,name = text_new.text.toString(), temp = text2.text.toString().toDouble(),pressure = text3.text.toString().toDouble(),windSpeed = text4.text.toString().toDouble(),cloud = text5.text.toString().toInt())

                withContext(Dispatchers.IO) { dao.update(s) }
                showAlert("Данные под ID: ${text6.text.toString().toLong()} сохранены","Отлично")
            }
        }
        button6.setOnClickListener{
            prefs.edit()
                .putString("username", "")
                .putString("password", "")
                .putBoolean("is_logged", false)
                .apply()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    fun showAlert(message: String,title:String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}