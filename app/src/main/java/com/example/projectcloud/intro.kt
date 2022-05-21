package com.example.projectcloud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_intro.*
import kotlin.math.log

class intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        btn_lecturer_intro.setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }
        btn_student_intro.setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }
    }
}