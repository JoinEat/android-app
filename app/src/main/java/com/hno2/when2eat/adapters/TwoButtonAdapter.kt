package com.hno2.when2eat.adapters


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hno2.when2eat.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TwoButtonAdapter(
        private val dataSet: MutableList<UnitData>, private val buttonText: MutableList<String>,
        private val mClickHandler: OnItemClickHandler
) : ButtonAdapter<TwoButtonAdapter.ViewHolder>(dataSet) {

    interface OnItemClickHandler {
        fun onItemClick(id: String, position: Int)
        suspend fun onBtn1Click(id: String, position: Int): Boolean
        suspend fun onBtn2Click(id: String, position: Int): Boolean
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val btn1: Button = itemView.findViewById(R.id.button1)
        val btn2: Button = itemView.findViewById(R.id.button2)
        val avatar: ImageView = itemView.findViewById(R.id.avatar)

        init {
            listItemView.setOnClickListener {
                mClickHandler.onItemClick(dataSet[absoluteAdapterPosition].id, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val listItemView = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_view_unit_with_two_buttons, viewGroup, false)
        return ViewHolder(listItemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val rowData = dataSet[position]
        val context = viewHolder.avatar.context

        viewHolder.nameTextView.text = rowData.name
        viewHolder.descriptionTextView.text = rowData.description
        viewHolder.btn1.text = buttonText[0]
        viewHolder.btn1.setOnClickListener {
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                val success = mClickHandler.onBtn1Click(rowData.id, position)
                if (success) removeItem(position)
            }
        }
        viewHolder.btn2.text = buttonText[1]
        viewHolder.btn2.setOnClickListener {
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                val success = mClickHandler.onBtn2Click(rowData.id, position)
                if (success) removeItem(position)
            }
        }
        Picasso.with(context).load(rowData.avatarUrl).into(viewHolder.avatar)
    }
}