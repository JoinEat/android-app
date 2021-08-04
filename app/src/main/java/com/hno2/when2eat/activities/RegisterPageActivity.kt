package com.hno2.when2eat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.tools.NetworkProcessor
import com.hno2.when2eat.tools.ToastMaker
import kotlinx.coroutines.*
import org.json.JSONObject


class RegisterPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)
        initViews()
        setListeners()
    }

    lateinit var registerButton: Button
    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText

    fun initViews() {
        registerButton = findViewById(R.id.registerButton)
        username = findViewById(R.id.registerPageUsername)
        email = findViewById(R.id.registerPageEmailAddress)
        password = findViewById(R.id.registerPagePassword)
    }

    private fun setListeners() {
        registerButton.setOnClickListener {
            val currentActivity = this
            val url = BuildConfig.Base_URL + "/users"

            val body = JSONObject()
            body.put("name", username.text)
            body.put("email", email.text)
            body.put("password", password.text)

            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                val returnedJSON = withContext(Dispatchers.IO) {
                    NetworkProcessor().sendRequest(
                            currentActivity,
                            Request.Method.POST,
                            url,
                            body,
                            ""
                    )
                }

                ToastMaker().toaster(this@RegisterPageActivity, returnedJSON)
                if (returnedJSON.getInt("statusCode") == 200) {
                    val loginIntent = Intent(currentActivity, LoginPageActivity::class.java)
                    startActivity(loginIntent)
                }
            }
        }
    }
}