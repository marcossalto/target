package com.marcossalto.targetmvd.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.ui.signin.SignInActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        goToSignIn()
    }

    private fun goToSignIn() {
        val splash = object : Thread(){
            override fun run(){
                try{
                    sleep(2500)
                    val intent = Intent(this@SplashActivity, SignInActivity::class.java)
                    finish()
                    startActivity(intent)
                }catch (e: Exception){}
            }
        }
        splash.start()
    }
}