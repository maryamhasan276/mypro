package com.example.projectcloud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.projectcloud.lecturer.home_lec
import com.example.projectcloud.model.user
import com.example.projectcloud.student.signupStudent
import com.example.projectcloud.utilities.SavedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private val userCollectionRef = Firebase.firestore.collection("user")
    private val lecturerCollectionRef = Firebase.firestore.collection("lecturer")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setEventListeners()
        initProperties()
    }

    private fun initProperties() {
        auth = FirebaseAuth.getInstance()
        email = ed_email_signIn.text.toString()
        password = ed_password_signIn.text.toString()
    }

    private fun setEventListeners() {
        btn_signIn.setOnClickListener {
            userSignIn()
        }
        tv_forget_password.setOnClickListener {
            startActivity(Intent(this, forget_password::class.java))
        }

        btn_signup_signIn.setOnClickListener {
            startActivity(Intent(this, signupStudent::class.java))
        }
    }

    private fun userSignIn() {
        // multiuser login redirecting to multi homes
        initProperties()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { p0 ->
                    if (p0.isSuccessful) {
                        val userId = p0.result.user!!.uid

                        val userCollectionRef = Firebase.firestore.collection("user").document(userId)

                        userCollectionRef.get().addOnCompleteListener {
                            val user = it.result.toObject<user>()
                            val uType = user!!.user_type
                            Log.d("uType", "userSignIn: $uType")

                            if (uType.equals("student")) {
                                startActivity(Intent(this, HomeStudentActivity::class.java))
                            } else {
                                startActivity(Intent(this, home_lec::class.java))
                            }
                        }
                        SavedPreferences.getEmail(this)
                    } else {
                        Log.e("===", "userSignI: a, $p0")
                    }
                }
                .addOnFailureListener {
                    Log.e("===", "addOnFailureListener: ${it.message}")
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}


