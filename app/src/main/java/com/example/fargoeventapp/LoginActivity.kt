package com.example.fargoeventapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation

/** Login Page **/
class LoginActivity : AppCompatActivity(){

    private var firstLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginLayout.visibility = View.INVISIBLE

        if (firstLogin) {
            firstLogin = false
            val preferences = getSharedPreferences(LOGIN, Context.MODE_PRIVATE)
            val username = preferences.getString(LOGIN_NAME, null)
            val password = preferences.getString(LOGIN_PASSWORD, null)
            EventAPI.login(this, username, password, this::onLoginResult)
            return
        }

        fadeInAndShowView(loginLayout)

    }

    override fun onBackPressed() {
        System.exit(0)
    }

    fun onLogin(view: View){
        val username = userNameText.text.toString()
        val password = passwordText.text.toString()
        login_button.isClickable = false
        EventAPI.login(this,username, password, this::onLoginResult)
    }

    //called by EventDataManager
    private fun onLoginResult(success: Boolean, message: String){
        if (message== FIRST_LOGIN){
            fadeInAndShowView(loginLayout)
            return
        }

        if(success){
            val intent = Intent(this, MainActivity::class.java)
                .putExtra(LOGIN_NAME, userNameText.text.toString())
                .putExtra(LOGIN_PASSWORD, userNameText.text.toString())

            finish()
            startActivity(intent)
            return
        }

        invalidLoginText.text = message
        fadeInAndShowView(invalidLoginText)
        login_button.isClickable = true
    }

    private fun fadeInAndShowView(view: View) {
        val fadeOut = AlphaAnimation(0f , 1f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 500

        fadeOut.setAnimationListener(object : AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationStart(animation: Animation) {}
        })

        view.startAnimation(fadeOut)
    }
    private fun fadeOutAndHideView(view: View) {
        val fadeOut = AlphaAnimation(0f , 1f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 1000

        fadeOut.setAnimationListener(object : AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationStart(animation: Animation) {}
        })

        view.startAnimation(fadeOut)
    }
}
