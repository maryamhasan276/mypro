package com.example.projectcloud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.projectcloud.student.profile
import com.example.projectcloud.utilities.SavedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_change_password.*

class Change_password : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var password: String
    private lateinit var newPassword: String
    private lateinit var confirmPassword: String
    private val userCollectionRef = Firebase.firestore.collection("user")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        initProperties()
        setEventsListeners()
    }

    private fun setEventsListeners() {
        back_changePassword.setOnClickListener {
            startActivity(Intent(this, profile::class.java))
            finish()
        }
        btn_save.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword(){
        initProperties()
        auth.currentUser!!.updatePassword(newPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("---", "changePassword: ${auth.currentUser}")
                Toast.makeText(this, "Successfully updated password", Toast.LENGTH_SHORT).show()
                SavedPreferences.password = newPassword
                startActivity(Intent(this, HomeStudentActivity::class.java))
            }
        }
            .addOnFailureListener {
                Log.e("---", "changePassword: $it")
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun initProperties() {
        auth = FirebaseAuth.getInstance()
        password = ed_current_password.text.toString()
        newPassword = ed_new_password.text.toString()
        confirmPassword = ed_repeat_password.text.toString()
    }
}

