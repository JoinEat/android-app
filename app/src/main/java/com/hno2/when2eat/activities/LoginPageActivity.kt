package com.hno2.when2eat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.NetworkProcessor
import com.hno2.when2eat.tools.ToastMaker
import kotlinx.coroutines.*
import org.json.JSONObject

class LoginPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        initViews()
        setListeners()
    }

    lateinit var loginButton: Button
    lateinit var email: EditText
    lateinit var password: EditText

    private fun initViews() {
        loginButton = findViewById(R.id.loginButton)
        email = findViewById(R.id.loginPageEmailAddress)
        password = findViewById(R.id.loginPagePassword)
    }

    private fun setListeners() {
        loginButton.setOnClickListener {
            val url = BuildConfig.Base_URL + "/users/login"
            val currentActivity = this

            val body = JSONObject()
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
                        ) }

                ToastMaker().toaster(this@LoginPageActivity, returnedJSON)
                if (returnedJSON.getInt("statusCode") == 200) {
                    DataSaver().setToken(currentActivity, returnedJSON.getString("token"))
                    DataSaver().setData(currentActivity, returnedJSON.getJSONObject("user"))

                    val selectIntent = Intent(this@LoginPageActivity, MainActivity::class.java)
                    startActivity(selectIntent)
                }
            }
        }
    }
}