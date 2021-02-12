package com.marcossalto.targetmvd.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.ui.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        goToSignUp()
    }

    private fun goToSignUp() {
        val splash = object : Thread(){
            override fun run(){
                try{
                    sleep(2500)
                    val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                    finish()
                    startActivity(intent)
                }catch (e: Exception){}
            }
        }
        splash.start()
    }
}