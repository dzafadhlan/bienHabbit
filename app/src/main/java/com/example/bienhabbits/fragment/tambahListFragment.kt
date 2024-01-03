package com.example.bienhabbits.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.bienhabbits.databinding.FragmentTambahListBinding
import com.example.bienhabbits.utils.datalist
import com.google.android.material.textfield.TextInputEditText


class tambahListFragment : DialogFragment() {

    private lateinit var binding: FragmentTambahListBinding
    private lateinit var simpan: dialog
    private var datalist: datalist? = null
    fun setTask(simpan: dialog) {
        this.simpan = simpan
    }

    companion object {
        const val TAG = "tambahListFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = tambahListFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentTambahListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            datalist = datalist(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString()
            )
            binding.todoEt.setText(datalist?.task)
        }
        registerEvent()
    }

    private fun registerEvent() {
        binding.todoNextBtn.setOnClickListener {
            val task = binding.todoEt.text.toString()
            if (task.isNotEmpty()) {
                if (datalist == null){
                    simpan.savTask(task, binding.todoEt)
                }else{
                    datalist?.task = task
                    simpan.editTask(datalist!!, binding.todoEt)
                }
            } else {
                Toast.makeText(context, "Enter Some Task!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }

    interface dialog {
        fun savTask(todo: String, todoEt: TextInputEditText)
        fun editTask(datalist: datalist, todoEt: TextInputEditText)
    }

}