package com.hno2.when2eat.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hno2.when2eat.R
import com.squareup.picasso.Picasso

class NoButtonAdapter(
        private val dataSet: MutableList<UnitData>,  private val mClickHandler: OnItemClickHandler
) : ButtonAdapter<NoButtonAdapter.ViewHolder>(dataSet) {

     interface OnItemClickHandler {
        fun onItemClick(id: String, position: Int)
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        init {
            listItemView.setOnClickListener {
                mClickHandler.onItemClick(dataSet[absoluteAdapterPosition].id,absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val listItemView = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_view_unit, viewGroup, false)
        return ViewHolder(listItemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val rowData = dataSet[position]
        val context = viewHolder.avatar.context

        viewHolder.nameTextView.text = rowData.name
        viewHolder.descriptionTextView.text = rowData.description
        Picasso.with(context).load(rowData.avatarUrl).into(viewHolder.avatar)
    }
}