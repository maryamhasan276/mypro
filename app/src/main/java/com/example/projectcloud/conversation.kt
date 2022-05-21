package com.example.projectcloud

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.ArrayMap
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectcloud.model.imagemessage
import com.example.projectcloud.model.textmessage
import com.example.projectcloud.model.user
import com.example.projectcloud.utilities.AppConstants
import com.example.projectcloud.utilities.FirestoreUtil
import com.example.projectcloud.utilities.SavedPreferences
import com.example.projectcloud.utilities.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.nl.smartreply.TextMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_conversation.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

private const val RC_SELECT_IMAGE = 2

class conversation : AppCompatActivity() {
    private lateinit var currentChannelId: String
    private lateinit var currentUser: user
    private lateinit var otherUserId: String

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME)

        // My Info
        FirestoreUtil.getCurrentUser {
            currentUser = it
        }

        // Other User Id
        otherUserId = intent.getStringExtra(AppConstants.USER_ID).toString()
        Log.e("TAG", "onCreate: $otherUserId")

        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            currentChannelId = channelId

            setChatUsers()

            messagesListenerRegistration =
                FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)

            imageView_send.setOnClickListener {
                val messageToSend =
                    textmessage(
                        editText_message.text.toString(), Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        otherUserId, currentUser.username
                    )
                editText_message.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }

            fab_send_image.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }
        }
    }

    fun setChatUsers() {
        val param = ArrayMap<String, Any>()

        val chatUsers = ArrayList<String>()
        val myId = SavedPreferences.getUser(this) ?: ""

        chatUsers.add(myId)
        chatUsers.add(otherUserId)

        param["usersIds"] = chatUsers
        Firebase.firestore.collection("chatChannels").document(currentChannelId).set(param, SetOptions.merge())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            StorageUtil().uploadMessageImage(selectedImageBytes) { imagePath ->

                val messageToSend =
                    imagemessage(
                        imagePath, Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        otherUserId, currentUser.username
                    )
                FirestoreUtil.sendMessage(messageToSend, currentChannelId)
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            rv_messages.apply {
                layoutManager = LinearLayoutManager(this@conversation)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        rv_messages.scrollToPosition(rv_messages.adapter!!.itemCount - 1)
    }
}

