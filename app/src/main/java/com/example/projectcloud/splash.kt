package com.example.projectcloud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth

class splash : AppCompatActivity() {
    var handler: Handler = Handler()
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setFlags()
        setHandler()
    }

    private fun setHandler() {
        handler.postDelayed({
            checkUserSignIn()
        }, 1000)
    }

    private fun checkUserSignIn() {
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeStudentActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, intro::class.java))
            finish()
        }
    }


    private fun setFlags() {
        this.window.setFlags(
            WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW,
            WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW
        )
    }
}


