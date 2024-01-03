package com.example.bienhabbits.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bienhabbits.databinding.DatahabBinding


class AdapterHabbit(private val list: MutableList<dataHab>) : RecyclerView.Adapter<AdapterHabbit.HabbitView>()  {
    private var listen : habAdapterClick ?= null



    class HabbitView (val binding: DatahabBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabbitView {
        val binding = DatahabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabbitView(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun click(listen: habAdapterClick){
        this.listen = listen
    }

    override fun onBindViewHolder(holder: HabbitView, position: Int) {
        with(holder){
            with(list[position]){
                binding.habbit.text = this.habbit
                binding.delete2.setOnClickListener {
                    listen?.onDelete(this)
                }
                binding.edit2.setOnClickListener {
                    listen?.onEdit(this)
                }
            }
        }
    }

    interface habAdapterClick {
        fun onDelete(dataHab: dataHab)
        fun onEdit(dataHab: dataHab)
    }

}