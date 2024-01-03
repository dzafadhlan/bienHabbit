package com.example.bienhabbits.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bienhabbits.databinding.DatalistBinding

class AdapterTodo(private val list: MutableList<datalist>) : RecyclerView.Adapter<AdapterTodo.TodoViewHolder>() {
    private var listener : ToDoAdapterClicks? = null
    fun setClicks(listener: ToDoAdapterClicks){
        this.listener = listener
    }
    class TodoViewHolder(val binding: DatalistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = DatalistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.task.text = this.task

                binding.delete.setOnClickListener {
                    listener?.onhapus(this)
                }
                binding.edit.setOnClickListener {
                    listener?.edit(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface ToDoAdapterClicks{
        fun onhapus(datalist: datalist)
        fun edit(datalist: datalist)
    }
}



