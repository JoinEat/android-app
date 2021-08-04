package com.hno2.when2eat.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class ButtonAdapter<T : RecyclerView.ViewHolder>(private val dataSet: MutableList<UnitData>) : RecyclerView.Adapter<T>() {
    override fun getItemCount() = dataSet.size

    fun removeItem(position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }
}