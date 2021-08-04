package com.hno2.when2eat.tools

import android.app.Dialog

import android.os.Bundle
import android.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.hno2.when2eat.R

class DialogMaker(private val message :String): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                    .setPositiveButton(R.string.ok
                    ) { _, _ ->
                    }
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}