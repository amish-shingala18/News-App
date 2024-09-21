package com.example.news

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.news.activity.BookmarkActivity
import com.example.news.activity.MediaActivity
import com.example.news.activity.SearchActivity
import com.example.news.adapter.NewsApiAdapter
import com.example.news.databinding.ActivityMainBinding
import com.example.news.domain.ApiClient.Companion.getApi
import com.example.news.domain.ApplicationNetwork
import com.example.news.helper.CountryHelper
import com.example.news.helper.SharedHelper
import com.example.news.interfaces.ApiInterface
import com.example.news.model.ArticlesItem
import com.example.news.model.NewsApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {
    private var selectedCountry: String?=null
    private lateinit var binding : ActivityMainBinding
    private var imageList = ArrayList<SlideModel>()
    private var newsList = mutableListOf<ArticlesItem>()
    private val countryHelper = CountryHelper()
    private lateinit var newsApiAdapter : NewsApiAdapter
    private var sharedHelper=SharedHelper()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        selectedCountry=countryHelper.getCountry(this@MainActivity)
        if(selectedCountry=="in"){
            binding.txtSetName.text="IND"
            binding.imgSetFlag.setImageResource(R.drawable.india_flag)
        }
        if(selectedCountry=="au"){
            binding.txtSetName.text="AUS"
            binding.imgSetFlag.setImageResource(R.drawable.australia_flag)
        }
        if(selectedCountry=="cn"){
            binding.txtSetName.text="CHINA"
            binding.imgSetFlag.setImageResource(R.drawable.china_flag)
        }
        if(selectedCountry=="jp"){
            binding.txtSetName.text="JAPAN"
            binding.imgSetFlag.setImageResource(R.drawable.japan_flag)
        }
        if(selectedCountry=="ca"){
            binding.txtSetName.text="CND"
            binding.imgSetFlag.setImageResource(R.drawable.canada_flag)
        }
        if(selectedCountry=="us"){
            binding.txtSetName.text="US"
            binding.imgSetFlag.setImageResource(R.drawable.us_flag)
        }
        setUpImageSlider()
        bottomNavigationBar()
        getCountryApi("general")
        initClick()
        startTheme()
        val theme = this.let { sharedHelper.getTheme(it) }
        binding.msTheme.isChecked = theme
        (application as ApplicationNetwork).liveData.observe(this@MainActivity){
            if(it){
                binding.clMainActivity.visibility=View.VISIBLE
                binding.menu.visibility=View.VISIBLE
                binding.imgMainNoNet.visibility=View.GONE
                binding.txtNoMainNet.visibility=View.GONE
                binding.txtMainNoNet2.visibility=View.GONE
            }
            else{
                binding.clMainActivity.visibility=View.GONE
                binding.menu.visibility=View.GONE
                binding.imgMainNoNet.visibility=View.VISIBLE
                binding.txtNoMainNet.visibility=View.VISIBLE
                binding.txtMainNoNet2.visibility=View.VISIBLE
            }
        }
    }
    private fun getCountryApi(categories:String){
        val retrofit=getApi().create(ApiInterface::class.java)
        retrofit.getCategoryNews(category = categories, country = selectedCountry).enqueue(object : Callback<NewsApiModel> {
            override fun onResponse(call: Call<NewsApiModel>, response: Response<NewsApiModel>) {
                if (response.isSuccessful){
                    if(response.body()!!.articles!!.isEmpty()){
                        binding.menu.visibility=View.VISIBLE
                        binding.imgMainNoResult.visibility=View.VISIBLE
                        binding.rvNews.visibility=View.GONE
                        binding.imgSlider.visibility=View.GONE
                        binding.textsRecommendation.visibility=View.GONE
                        binding.textsBN.visibility=View.GONE
                        binding.cartView.visibility=View.GONE
                    }else {
                        binding.imgMainNoResult.visibility=View.GONE
                        val l1 = response.body()!!.articles as MutableList<ArticlesItem>

                        newsList = l1.filter {
                            it.title!="[Removed]"
                        }.toMutableList()
                        newsApiAdapter = NewsApiAdapter(newsList)
                        binding.rvNews.adapter = newsApiAdapter
                        Log.d("TAG", "onResponse: ${response.body()}")
                    }
                }
            }
            override fun onFailure(call: Call<NewsApiModel>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }
    private fun setUpImageSlider() {
        val retrofit = getApi().create(ApiInterface::class.java)
        retrofit.getCarousalNews(q = "everything").enqueue(object : Callback<NewsApiModel> {
            override fun onResponse(call: Call<NewsApiModel>, response: Response<NewsApiModel>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    articles?.let { articlesList ->
                        imageList = ArrayList()
                        for (x in articlesList) {
                            if (!x!!.urlToImage.isNullOrEmpty()) {
                                imageList.add(SlideModel(x.urlToImage, x.title ?: ""))
                            }
                        }
                        if (imageList.isNotEmpty()) {
                            binding.imgSlider.setImageList(imageList, ScaleTypes.FIT)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<NewsApiModel>, t: Throwable) {}
        })
    }
    private fun bottomNavigationBar(){
        binding.menu.setItemSelected(R.id.home,true)
        binding.menu.setOnItemSelectedListener {
            when(it){
                R.id.home ->{
                    binding.menu.setItemSelected(R.id.home,true)
                }
                R.id.search ->{
                    binding.menu.setItemSelected(R.id.search,true)
                    startActivity(Intent(this, SearchActivity::class.java))
                }
                R.id.media -> {
                    binding.menu.setItemSelected(R.id.media, true)
                    startActivity(Intent(this, MediaActivity::class.java))
                }
                R.id.bookmark -> {

                    binding.menu.setItemSelected(R.id.bookmark, true)
                    startActivity(Intent(this, BookmarkActivity::class.java))
                }
                else -> {
                    binding.menu.setItemSelected(R.id.home,true)
                }
            }
        }
    }
    @SuppressLint("ResourceAsColor")
    private fun initClick(){
        binding.cvGeneralCat.setOnClickListener {
            getCountryApi("general")
            binding.cvGeneralCat.setBackgroundResource(R.drawable.selected_category)
            binding.cvEntertainmentCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvBusinessCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvHealthCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvSportsCat.setBackgroundResource(R.drawable.unselected_category)
            binding.textGeneral.setTextColor(R.color.bottom_nav)
            binding.textEnter.setTextColor(R.color.text_change)
            binding.textBusiness.setTextColor(R.color.text_change)
            binding.textHealth.setTextColor(R.color.text_change)
            binding.textSports.setTextColor(R.color.text_change)
        }
        binding.cvEntertainmentCat.setOnClickListener {
            getCountryApi("entertainment")
            binding.cvEntertainmentCat.setBackgroundResource(R.drawable.selected_category)
            binding.cvGeneralCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvBusinessCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvHealthCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvSportsCat.setBackgroundResource(R.drawable.unselected_category)
            binding.textGeneral.setTextColor(R.color.text_change)
            binding.textEnter.setTextColor(R.color.bottom_nav)
            binding.textBusiness.setTextColor(R.color.text_change)
            binding.textHealth.setTextColor(R.color.text_change)
            binding.textSports.setTextColor(R.color.text_change)
        }
        binding.cvBusinessCat.setOnClickListener {
            getCountryApi("business")
            binding.cvBusinessCat.setBackgroundResource(R.drawable.selected_category)
            binding.cvGeneralCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvEntertainmentCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvHealthCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvSportsCat.setBackgroundResource(R.drawable.unselected_category)
            binding.textGeneral.setTextColor(R.color.text_change)
            binding.textEnter.setTextColor(R.color.text_change)
            binding.textBusiness.setTextColor(R.color.bottom_nav)
            binding.textHealth.setTextColor(R.color.text_change)
            binding.textSports.setTextColor(R.color.text_change)
        }
        binding.cvHealthCat.setOnClickListener {
            getCountryApi("health")
            binding.cvHealthCat.setBackgroundResource(R.drawable.selected_category)
            binding.cvGeneralCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvEntertainmentCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvBusinessCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvSportsCat.setBackgroundResource(R.drawable.unselected_category)
            binding.textGeneral.setTextColor(R.color.text_change)
            binding.textEnter.setTextColor(R.color.text_change)
            binding.textBusiness.setTextColor(R.color.text_change)
            binding.textHealth.setTextColor(R.color.bottom_nav)
            binding.textSports.setTextColor(R.color.text_change)
        }
        binding.cvSportsCat.setOnClickListener {
            getCountryApi("sports")
            binding.cvSportsCat.setBackgroundResource(R.drawable.selected_category)
            binding.cvGeneralCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvEntertainmentCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvBusinessCat.setBackgroundResource(R.drawable.unselected_category)
            binding.cvHealthCat.setBackgroundResource(R.drawable.unselected_category)
            binding.textGeneral.setTextColor(R.color.text_change)
            binding.textEnter.setTextColor(R.color.text_change)
            binding.textBusiness.setTextColor(R.color.text_change)
            binding.textHealth.setTextColor(R.color.text_change)
            binding.textSports.setTextColor(R.color.bottom_nav)
        }
        binding.imgDrawerMenu.setOnClickListener {
            binding.main.openDrawer(GravityCompat.START)
        }
        binding.msTheme.setOnCheckedChangeListener { _, isChecked ->
            addTheme(isChecked)
            sharedHelper.setTheme(this,isChecked)
        }
        binding.lnrCountry.setOnClickListener {
            countryDialog()
        }
    }
    private fun addTheme(theme:Boolean){
        if (theme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    private fun startTheme(){
        val theme =sharedHelper.getTheme(this)
        if (theme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    private fun countryDialog(){
        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.country_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val dialogIndia = dialog.findViewById<LinearLayout>(R.id.dialogIndia)
        val dialogAustralia = dialog.findViewById<LinearLayout>(R.id.dialogAustralia)
        val dialogChina = dialog.findViewById<LinearLayout>(R.id.dialogChina)
        val dialogJapan = dialog.findViewById<LinearLayout>(R.id.dialogJapan)
        val dialogCanada = dialog.findViewById<LinearLayout>(R.id.dialogCanada)
        val dialogUS = dialog.findViewById<LinearLayout>(R.id.dialogUS)

        dialogIndia.setOnClickListener {
            selectedCountry="in"
            countryHelper.setCountry(this@MainActivity,selectedCountry!!)
            selectedCountry=countryHelper.getCountry(this@MainActivity)
            dialog.dismiss()
        }
        dialogAustralia.setOnClickListener {
            selectedCountry="au"
            countryHelper.setCountry(this@MainActivity,selectedCountry!!)
            selectedCountry=countryHelper.getCountry(this@MainActivity)
            dialog.dismiss()
        }
        dialogChina.setOnClickListener {
            selectedCountry="cn"
            countryHelper.setCountry(this@MainActivity,selectedCountry!!)
            selectedCountry=countryHelper.getCountry(this@MainActivity)
            dialog.dismiss()
        }
        dialogJapan.setOnClickListener {
            selectedCountry="jp"
            countryHelper.setCountry(this@MainActivity,selectedCountry!!)
            selectedCountry=countryHelper.getCountry(this@MainActivity)
            dialog.dismiss()
        }
        dialogCanada.setOnClickListener {
            selectedCountry="ca"
            countryHelper.setCountry(this@MainActivity,selectedCountry!!)
            selectedCountry=countryHelper.getCountry(this@MainActivity)
            dialog.dismiss()
        }
        dialogUS.setOnClickListener {
            selectedCountry="us"
            countryHelper.setCountry(this@MainActivity,selectedCountry!!)
            selectedCountry=countryHelper.getCountry(this@MainActivity)
            dialog.dismiss()
        }
    }
    override fun onResume() {
        binding.menu.setItemSelected(R.id.home,true)
        super.onResume()
    }
}