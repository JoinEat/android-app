package com.hno2.when2eat.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hno2.when2eat.R
import com.hno2.when2eat.tools.DataSaver

class SelectPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_page)

        if (DataSaver().getToken(this).equals("")) {
            val selectIntent = Intent(this, WelcomePageActivity::class.java)
            startActivity(selectIntent)
        } else {
            val selectIntent = Intent(this, MainActivity::class.java)
            startActivity(selectIntent)
        }
    }
}