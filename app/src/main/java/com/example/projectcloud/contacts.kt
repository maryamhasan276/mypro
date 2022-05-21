package com.example.projectcloud

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.projectcloud.model.user
import com.example.projectcloud.utilities.SavedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_contacts.*

class contacts : AppCompatActivity() {
    private lateinit var contactsFragment: contactfragments
    private val userCollectionRef = Firebase.firestore.collection("user")
    private lateinit var auth: FirebaseAuth
    private lateinit var userImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        retrieveUser()

        contactsFragment = contactfragments()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, contactsFragment)
                .commit()
        }
    }

    private fun retrieveUser() {
        auth = FirebaseAuth.getInstance()
        userImage = profile_std_chat
        val uid = SavedPreferences.user_id
        if (uid != "") {
            userCollectionRef.document(uid!!).get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val user = document.toObject<user>()
                        Glide.with(this).load(Uri.parse(user!!.user_image)).circleCrop()
                            .into(userImage)
                    } else {
                        Log.d("---", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("---", "get failed with ", exception)
                }
        }
    }
}


