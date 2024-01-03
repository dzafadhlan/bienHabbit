package com.example.bienhabbits.fragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bienhabbits.R
import com.example.bienhabbits.databinding.FragmentTimerBinding
import com.example.bienhabbits.utils.dataTimer
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.collections.ArrayList


class timerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding
    private lateinit var dataTimer: dataTimer
    private val timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.VISIBLE
        binding = FragmentTimerBinding.inflate(inflater,container,false)
        val view = binding.root
        dataTimer = dataTimer(requireContext())
        binding.playBtn.setOnClickListener { startStop() }
        binding.resetBtn.setOnClickListener { reset() }

        if (dataTimer.timerCounting()){
            startTime()
        }else{
            stopTime()
            if ((dataTimer.startTime() != null && dataTimer.stopTime() != null)){
                val time = Date().time - calculateRestartTime().time
                binding.timeView.text = timeStringFromLong(time)
            }
        }
        timer.scheduleAtFixedRate(TimeTask(), 0, 500)
        return view
    }



    private inner class TimeTask: TimerTask()
    {
        override fun run()
        {
            activity?.runOnUiThread {
                if (dataTimer.timerCounting()) {
                    val time = Date().time - dataTimer.startTime()!!.time
                    binding.timeView.text = timeStringFromLong(time)
                }
            }
        }
    }
    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    private fun stopTime() {
        dataTimer.setTimerCounting(false)
        binding.playBtn.setImageResource(R.drawable.baseline_play_arrow_24)
    }

    private fun startTime() {
        dataTimer.setTimerCounting(true)
        binding.playBtn.setImageResource(R.drawable.baseline_pause_24)
    }

    private fun reset() {
        dataTimer.setStopTime(null)
        dataTimer.setStartTime(null)
        stopTime()
        binding.timeView.text = timeStringFromLong(0)
    }



    private fun startStop() {
        if(dataTimer.timerCounting())
        {
            dataTimer.setStopTime(Date())
            stopTime()
        }
        else
        {
            if(dataTimer.stopTime() != null)
            {
                dataTimer.setStartTime(calculateRestartTime())
                dataTimer.setStopTime(null)
            }
            else
            {
                dataTimer.setStartTime(Date())
            }
            startTime()
        }
    }

    private fun calculateRestartTime(): Date {
        val diff = dataTimer.startTime()!!.time - dataTimer.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

}
