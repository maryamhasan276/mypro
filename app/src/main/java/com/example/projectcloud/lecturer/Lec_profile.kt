package com.example.projectcloud.lecturer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.projectcloud.Change_password
import com.example.projectcloud.R
import com.example.projectcloud.utilities.SavedPreferences
import com.example.projectcloud.login
import com.example.projectcloud.model.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_lec_profile.*

class lec_profile : AppCompatActivity() {
    private lateinit var profileImage: ImageView
    private val lecturerCollectionRef = Firebase.firestore.collection("lecturer")
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lec_profile)
        initProperties()
        setEventsListeners()
        retrieveUser()
        profileImage = iv_lec_profile
    }

    private fun retrieveUser() {
        val uid = SavedPreferences.user_id

        if (uid != "") {
            lecturerCollectionRef.document(uid!!).get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val user = document.toObject<user>()
                        tv_lec_name.text = user!!.username
                        tv_lec_email.text = user.email
                        Glide.with(this).load(Uri.parse(user.user_image)).circleCrop().into(iv_lec_profile)
                    } else {
                        Log.d("---", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("---", "get failed with ", exception)
                }
        }
    }

    private fun setEventsListeners() {
        btn_lec_change_password.setOnClickListener {
            startActivity(Intent(this, Change_password::class.java))
            finish()
        }
        btn_lec_edit_profile.setOnClickListener {
            startActivity(Intent(this, lec_edit_profile::class.java))
            finish()
        }
        lec_back_profile.setOnClickListener {
            startActivity(Intent(this, home_lec::class.java))
        }
        tv_lec_logout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, login::class.java))
        }
    }

    private fun initProperties() {
        auth = FirebaseAuth.getInstance()
    }
}


