package com.example.projectcloud.model

import android.content.Context
import com.bumptech.glide.Glide
import com.example.projectcloud.R
import com.google.firebase.firestore.auth.User
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chatlistitem.view.*

class personalitem (

    val person: user,
    val userId: String,
    private val context: Context
) :
    Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tv_name.text = person.username
        viewHolder.itemView.tv_bio.text = person.email
        if (person.user_image != null)
            Glide.with(context)
                .load(person.user_image).circleCrop()
                .placeholder(R.drawable.circle)
                .into(viewHolder.itemView.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.activity_chatlistitem
}
