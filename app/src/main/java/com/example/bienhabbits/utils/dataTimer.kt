package com.example.bienhabbits.utils

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.bienhabbits.R
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class dataTimer(context: Context) {
    private var sharedPref : SharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private var dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())

    private var timerCounting = false
    private var startTime: Date? = null
    private var stopTime: Date? = null



    init
    {
        timerCounting = sharedPref.getBoolean(COUNTING_KEY, false)

        val startString = sharedPref.getString(START_TIME_KEY, null)
        if (startString != null)
            startTime = dateFormat.parse(startString)

        val stopString = sharedPref.getString(STOP_TIME_KEY, null)
        if (stopString != null)
            stopTime = dateFormat.parse(stopString)
    }


    fun startTime(): Date? = startTime

    fun setStartTime(date: Date?)
    {
        startTime = date
        with(sharedPref.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(START_TIME_KEY,stringDate)
            apply()
        }
    }



    fun stopTime(): Date? = stopTime

    fun setStopTime(date: Date?)
    {
        stopTime = date
        with(sharedPref.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(STOP_TIME_KEY,stringDate)
            apply()
        }
    }

//    fun setSaveTimer(position: Int, convertView: View?, parent: ViewGroup):View{
//        var lisTim = convertView
//        if (lisTim == null){
//            lisTim = LayoutInflater.from(context).inflate(R.layout.datalist,parent,false)
//        }
//    }






    fun timerCounting(): Boolean = timerCounting

    fun setTimerCounting(value: Boolean)
    {
        timerCounting = value
        with(sharedPref.edit())
        {
            putBoolean(COUNTING_KEY,value)
            apply()
        }
    }


    companion object
    {
        const val PREFERENCES = "prefs"
        const val START_TIME_KEY = "startKey"
        const val STOP_TIME_KEY = "stopKey"
        const val COUNTING_KEY = "countingKey"
        const val  SAVE_TIMER_KEY = "saveKey"
    }
}