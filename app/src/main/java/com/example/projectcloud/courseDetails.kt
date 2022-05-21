package com.example.projectcloud

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.projectcloud.Adapter.lecturerAdapter
import com.example.projectcloud.model.course
import com.example.projectcloud.model.lecture
import com.example.projectcloud.utilities.SavedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_course_details.*

class courseDetails : AppCompatActivity() , lecturerAdapter.SetClickListener {
    private lateinit var lecturesAdapter: lecturerAdapter
    private val coursesCollectionRef = Firebase.firestore.collection("courses")
    private val lecturesCollectionRef = Firebase.firestore.collection("lectures")

    var course_key: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)
        retrieveCourseDetails()

        course_key = intent.getStringExtra("course_key").toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveCourseDetails() {

        val uid = SavedPreferences.user_id

        if (uid != "") {
            coursesCollectionRef.get()
                .addOnCompleteListener {

                    val array = arrayListOf<course>()
                    for (course in it.result.documents) {
                        val obj = course.toObject<course>()
                        array.add(obj!!)
                        tv_course_title.text = obj.course_name
                        tv_course_desc.text = obj.course_desc
                        tv_lecturer_name.text = obj.course_instructor
                        Glide.with(applicationContext).load(obj.course_image).into(iv_course_icon)
                    }

                    Log.d("documents", "$array")

                    lecturesCollectionRef.whereEqualTo("course_key", course_key).get()
                        .addOnCompleteListener {
                            try {
                                val lectures = arrayListOf<lecture>()
                                for (lecture in it.result.documents) {
                                    val docs = lecture.toObject<lecture>()
                                    lectures.add(docs!!)
                                }
                                lecturesAdapter = lecturerAdapter(this, lectures)
                                lecturesAdapter.setListener(this)
                                rv_lectures.adapter = lecturesAdapter
                                lecturesAdapter.notifyDataSetChanged()
                            } catch (e: Exception) {
                                Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
                            }
                        }
                }
                .addOnFailureListener { exception ->
                    Log.d("---", "get failed with ", exception)
                }
        }
    }

    override fun onItemClickListener(position: Int, lecture: lecture) {
        startActivity(Intent(this, lec_detail::class.java))
    }
}
