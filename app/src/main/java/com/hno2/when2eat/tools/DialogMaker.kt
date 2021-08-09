package com.hno2.when2eat.tools

import android.app.Dialog

import android.os.Bundle
import android.app.AlertDialog
import android.content.Context
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hno2.when2eat.R
import org.json.JSONObject

class DialogMaker(private val c: Context, private val data: JSONObject): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val content = inflater.inflate(R.layout.dialog_user,null)
            val username = DataParser().friendHandleParser(c,data)
            val description = DataParser().friendDataParser(c,data)

            builder.setView(content)
                    .setPositiveButton(R.string.ok
                    ) { _, _ ->
                    }
            content.findViewById<TextView>(R.id.name).text = username
            content.findViewById<TextView>(R.id.message).text = description
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}