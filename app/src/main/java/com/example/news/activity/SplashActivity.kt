package com.example.news.activity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.helper.CountryHelper

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val countryHelper=CountryHelper()
    private var isSelected:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handler()
    }
    private fun handler() {
        Handler(Looper.getMainLooper()).postDelayed({
            isSelected=countryHelper.getCountry(this@SplashActivity)
            if(isSelected==null){
                val intent = Intent(this@SplashActivity, CountryActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        },5000)
    }
}