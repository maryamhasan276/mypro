package com.example.projectcloud.student

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.ArrayMap
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projectcloud.R
import com.example.projectcloud.utilities.SavedPreferences
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class edit_profile : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var username: String
    private lateinit var userImage: ImageView
    private lateinit var email: String
    private var new_image: Boolean = false
    private var image_uri: Uri? = null

    private val userCollectionRef = Firebase.firestore.collection("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setEventsListeners()
        auth = FirebaseAuth.getInstance()
        username = ed_std_name_editProfile.text.toString()
        userImage = iv_std_edit_profile
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
    }

    private fun setEventsListeners() {
        back_std_editProfile.setOnClickListener {
            startActivity(Intent(this, profile::class.java))
            finish()
        }

        btn_save.setOnClickListener {
            saveUser()
        }
        iv_std_edit_profile.setOnClickListener {
            createDialog()
        }
    }

    private fun createDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Image")
        builder.setMessage("")
        builder.setPositiveButton("Gallery") { dialogInterface, which ->
            pickImageFromGallery()
        }
        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("Camera") { dialogInterface, which ->
//            captureImage()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun saveUser()  = CoroutineScope(Dispatchers.IO).launch {
        try {
            val uid = auth.currentUser!!.uid
            Log.e("uid", "saveUser: $uid")

            val param = ArrayMap<String, Any>()
            param["username"] = username

            if (new_image) {
                uploadImage(image_uri!!)
            } else {
                userCollectionRef.document(uid).update(param)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@edit_profile, "Successfully saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@edit_profile, profile::class.java))
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@edit_profile, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                iv_std_edit_profile.setImageURI(it)

                new_image = true
                image_uri = it
                showSelectedImage(it)
            }
        }
    }

    private fun showSelectedImage(filePath: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            iv_std_edit_profile.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun uploadImage(filePath: Uri) {
        val ref = storageReference?.child("uploads/" + auth.currentUser!!.uid)
        val uploadTask = ref?.putFile(filePath)

        val urlTask = uploadTask?.continueWithTask(
            Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                addUploadRecordToDb(downloadUri.toString())
            } else {
                Toast.makeText(this@edit_profile, "${task.result}", Toast.LENGTH_SHORT).show()
            }
        }?.addOnFailureListener {
        }
    }

    private fun addUploadRecordToDb(uri: String) {
        username = ed_std_name_editProfile.text.toString()
        var data = ArrayMap<String, Any>()
        data["user_image"] = uri
        data["username"] = username

        userCollectionRef.document(SavedPreferences.user_id!!).update(data)

    }
}



