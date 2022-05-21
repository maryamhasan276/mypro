package com.example.projectcloud.student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import com.example.projectcloud.HomeStudentActivity
import com.example.projectcloud.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup_student.*

class signupStudent : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var studentId: String
    private lateinit var username: String
    private lateinit var uType: String
    private val userCollectionRef = Firebase.firestore.collection("user")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_student)
        initProperties()
        btn_signup.setOnClickListener {
            userSignup()
        }
    }

    private fun initProperties() {
        auth = FirebaseAuth.getInstance()
        email = ed_email_signup.text.toString()
        studentId = ed_student_id.text.toString()
        password = ed_password_signup.text.toString()
        username = ed_name_signup.text.toString()
        uType = ed_uType.text.toString()
    }

    private fun userSignup() {
        initProperties()
        if (email.isNotEmpty() && password.isNotEmpty() && studentId.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { p0 ->
                    if (p0.isSuccessful) {

                        val uid = p0.result.user!!.uid
                        val ref = userCollectionRef.document(uid)

                        val param = ArrayMap<String, Any>()
                        param["username"] = username
                        param["email"] = email
                        param["student_id"] = studentId
                        param["user_id"] = uid
                        param["user_type"] = uType

                        ref.set(param)

                        Log.e("===", "onComplete: $p0")
                        val intent = (Intent(this, HomeStudentActivity::class.java))
                        intent.putExtra("user_id", uid)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("===", "onComplete: ")
                    }
                }.addOnFailureListener {
                    Log.e("===", "addOnFailureListener: ${it.message}")
                }
        }
    }
}


