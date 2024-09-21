package com.example.news.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.databinding.ActivityCountryBinding
import com.example.news.domain.ApplicationNetwork
import com.example.news.helper.CountryHelper

class CountryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountryBinding
    private var selectedCountry: String? = null
    private val countryHelper = CountryHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        (application as ApplicationNetwork).liveData.observe(this@CountryActivity){
            if(it){
                binding.linearCountry.visibility=View.VISIBLE
                binding.lnrCountry2.visibility=View.VISIBLE
                binding.lnrCountry3.visibility=View.VISIBLE
                binding.textsSelectCountry.visibility=View.VISIBLE
                binding.cvCountrySelected.visibility=View.VISIBLE
                binding.imgCountryNoNet.visibility=View.GONE
                binding.txtNoCountryNet.visibility=View.GONE
                binding.txtCountryNoNet2.visibility=View.GONE
            }
            else{
                binding.linearCountry.visibility=View.GONE
                binding.lnrCountry2.visibility=View.GONE
                binding.lnrCountry3.visibility=View.GONE
                binding.textsSelectCountry.visibility=View.GONE
                binding.cvCountrySelected.visibility=View.GONE
                binding.imgCountryNoNet.visibility=View.VISIBLE
                binding.txtNoCountryNet.visibility=View.VISIBLE
                binding.txtCountryNoNet2.visibility=View.VISIBLE
            }
        }
        selectCountry()
    }
    private fun selectCountry() {
        binding.cvCountrySelected.setOnClickListener {
            if (selectedCountry != null) {
                countryHelper.setCountry(this, selectedCountry!!)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("country", selectedCountry)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a country first!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.cvIndia.setOnClickListener {
            selectedCountry = "in"
            updateCountrySelection(R.drawable.selected_country, binding.lnrIndia)
        }
        binding.cvAustralia.setOnClickListener {
            selectedCountry = "au"
            updateCountrySelection(R.drawable.selected_country, binding.lnrAustralia)
        }
        binding.cvChina.setOnClickListener {
            selectedCountry = "cn"
            updateCountrySelection(R.drawable.selected_country, binding.lnrChina)
        }
        binding.cvJapan.setOnClickListener {
            selectedCountry = "jp"
            updateCountrySelection(R.drawable.selected_country, binding.lnrJapan)
        }
        binding.cvCanada.setOnClickListener {
            selectedCountry = "ca"
            updateCountrySelection(R.drawable.selected_country, binding.lnrCanada)
        }
        binding.cvUS.setOnClickListener {
            selectedCountry = "us"
            updateCountrySelection(R.drawable.selected_country, binding.lnrUS)
        }
    }
    private fun updateCountrySelection(selectedCountry: Int, selectedLayout: View) {
        binding.lnrIndia.setBackgroundResource(R.drawable.unselected_country)
        binding.lnrAustralia.setBackgroundResource(R.drawable.unselected_country)
        binding.lnrChina.setBackgroundResource(R.drawable.unselected_country)
        binding.lnrJapan.setBackgroundResource(R.drawable.unselected_country)
        binding.lnrCanada.setBackgroundResource(R.drawable.unselected_country)
        binding.lnrUS.setBackgroundResource(R.drawable.unselected_country)
        selectedLayout.setBackgroundResource(selectedCountry)
    }
}

