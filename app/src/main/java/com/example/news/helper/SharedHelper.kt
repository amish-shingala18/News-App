package com.example.news.helper

import android.app.Activity

class SharedHelper {
    fun setTheme(activity:Activity,theme:Boolean){
        val pref =activity.getSharedPreferences("theme",Activity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("theme",theme)
        editor.apply()
    }
    fun getTheme(activity: Activity): Boolean {
        val pref=activity.getSharedPreferences("theme",Activity.MODE_PRIVATE)
        return pref.getBoolean("theme",false)
    }
}
class CountryHelper{
    fun setCountry(activity: Activity,country:String) {
        val pref = activity.getSharedPreferences("country", Activity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("country", country)
        editor.apply()
    }
    fun getCountry(activity: Activity): String? {
        val pref = activity.getSharedPreferences("country", Activity.MODE_PRIVATE)
        return pref.getString("country", null)
    }
}