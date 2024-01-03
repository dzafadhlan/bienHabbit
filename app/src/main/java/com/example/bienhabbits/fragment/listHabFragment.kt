package com.example.bienhabbits.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.bienhabbits.R
import com.example.bienhabbits.databinding.FragmentListHabBinding
import com.example.bienhabbits.utils.dataHab
import com.example.bienhabbits.utils.datalist
import com.google.android.material.textfield.TextInputEditText


class listHabFragment : DialogFragment() {

    private lateinit var binding : FragmentListHabBinding
    private lateinit var setHab : dialog
    private var dataHab : dataHab? = null

    fun setHab(setHab : dialog){
        this.setHab = setHab
    }

    companion object{
        const val TAG = "listHabFragment"

        @JvmStatic
        fun newInstace(habId: String, habbit:String) = listHabFragment().apply {
            arguments = Bundle().apply{
                putString("habId", habId)
                putString("habbit", habbit)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListHabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null){
            dataHab = dataHab(
                arguments?.getString("habId").toString(),
                arguments?.getString("habbit").toString()
            )
            binding.habEt.setText(dataHab?.habbit)
        }
        registerEvent()
    }

    private fun registerEvent() {
        binding.todoNextBtn.setOnClickListener {
            val habbit = binding.habEt.text.toString()
            if (habbit.isNotEmpty()){
                if (dataHab == null){
                    setHab.saveHab(habbit, binding.habEt)
                }else{
                    dataHab?.habbit = habbit
                    setHab.editHab(dataHab!!, binding.habEt)
                }

            }else{
                Toast.makeText(context, "Enter Your Daily Habbit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface dialog {
        fun saveHab(hab: String, habET: TextInputEditText)
        fun editHab(dataHab: dataHab, habEt: TextInputEditText)
    }


}