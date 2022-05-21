package com.example.projectcloud.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectcloud.R
import com.example.projectcloud.contactfragments
import com.example.projectcloud.model.chatlist
import kotlinx.android.synthetic.main.activity_chatlistitem.view.*

class ChatlistAdapter (val context: contactfragments, val data: ArrayList<chatlist>) :
    RecyclerView.Adapter<ChatlistAdapter.ViewHolder>() {
    private var listener: SetClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chatImage: ImageView = itemView.imageView_profile_picture
        var chatName: TextView = itemView.tv_name
        var chatBio: TextView = itemView.tv_bio
        var chatCard: CardView = itemView.cv_person
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chatlistitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chatName.text = data[position].chat_username
        holder.chatBio.text = data[position].chat_bio
        holder.chatCard.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: clicked")
            listener?.onItemClickListener(position, data[position])
        }
        Glide.with(context).load(data[position].chat_userImage).circleCrop().into(holder.chatImage)
    }

    public fun setListener(listener: SetClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = data.size

    interface SetClickListener {
        fun onItemClickListener(position: Int, chatlist: chatlist)
    }
}
