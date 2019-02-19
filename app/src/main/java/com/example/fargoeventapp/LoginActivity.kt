package com.example.fargoeventapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

/** Login Page **/
class LoginActivity : AppCompatActivity() {

    val LOGIN_NAME = "username"
    val LOGIN_PASSWORD = "password"

    private var mLoginValid: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLogin(view: View?){
        //test
        mLoginValid = userNameText.text.toString() == "bob"

        if(mLoginValid){

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
