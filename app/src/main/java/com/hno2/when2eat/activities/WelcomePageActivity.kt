package com.hno2.when2eat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.hno2.when2eat.R


class WelcomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome_page)
        initViews()
        setListeners()
    }

    lateinit var loginButton: Button
    lateinit var registerButton: Button

    fun initViews() {
        loginButton = findViewById(R.id.to_login_page_button)
        registerButton = findViewById(R.id.to_register_page_button)
    }

    fun setListeners() {
        loginButton.setOnClickListener {
            val loginIntent = Intent(this, LoginPageActivity::class.java)
            startActivity(loginIntent)
        }

        registerButton.setOnClickListener {
            val registerIntent = Intent(this, RegisterPageActivity::class.java)
            startActivity(registerIntent)
        }
    }
}