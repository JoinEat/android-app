package com.hno2.when2eat.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hno2.when2eat.R
import com.hno2.when2eat.tools.DataSaver

class ChatAdapter(private val authorID: String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataSet: MutableList<ChatUnit> = mutableListOf()
    inner class ViewHolderSelf(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val content: TextView = itemView.findViewById(R.id.content)
    }

    inner class ViewHolderOthers(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val author: TextView = itemView.findViewById(R.id.author)
        val content: TextView = itemView.findViewById(R.id.content)
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position].authorID) {
            authorID -> 0
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> ViewHolderSelf(LayoutInflater.from(parent.context).inflate(R.layout.chat_unit_self, parent, false))
            else -> ViewHolderOthers(LayoutInflater.from(parent.context).inflate(R.layout.chat_unit, parent, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val rowData = dataSet[position]
        when (viewHolder.itemViewType) {
            0 -> {
                val holderSelf: ViewHolderSelf = viewHolder as ViewHolderSelf
                holderSelf.content.text = rowData.content
            }
            else -> {
                val holderOthers: ViewHolderOthers = viewHolder as ViewHolderOthers
                holderOthers.author.text = rowData.author
                holderOthers.content.text = rowData.content
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun appendMessages(messages: MutableList<ChatUnit>) {
        val originalSize = itemCount
        dataSet.addAll(messages)
        notifyItemRangeInserted(originalSize,messages.size)
    }
}