package com.example.fargoeventapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*

/** Login Page **/
class LoginActivity : AppCompatActivity(), EventDataManager.Loginable {

    val LOGIN_NAME = "username"
    val LOGIN_PASSWORD = "password"
    lateinit var usernameView: EditText
    lateinit var passwordView: EditText

    private var loginValid: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usernameView = userNameText
        passwordView = passwordText
    }

    override fun onBackPressed() {
        System.exit(0)
    }

    fun onLogin(view: View?){
        val eventDataManager = EventDataManager(this)
        val username = userNameText.text.toString()
        val password = passwordText.text.toString()
        eventDataManager.login(username, password)
    }

    //called by EventDataManager
    override fun onLoginResult(success: Boolean){
        if(success){
            val intent = Intent(this, MainActivity::class.java)
                .putExtra(LOGIN_NAME, userNameText.text.toString())
                .putExtra(LOGIN_PASSWORD, userNameText.text.toString())

            startActivity(intent)
        }
        else {
            invalidLogin_text.visibility = View.VISIBLE
        }
    }
}
