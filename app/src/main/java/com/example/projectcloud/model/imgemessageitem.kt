package com.example.projectcloud.model

import android.content.Context
import com.bumptech.glide.Glide
import com.example.projectcloud.R

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_item_imge_message.view.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_item_imge_message.view.*

class imgemessageitem (

    val message: imagemessage,
    val context: Context
) :
    messageitem(message) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)


        Glide.with(context)
            .load(message.imagePath).centerCrop()
            .placeholder(R.drawable.ic_baseline_insert_photo_24)
            .into(viewHolder.itemView.imageView_message_image)
    }

    override fun getLayout() = R.layout.activity_item_imge_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is imgemessageitem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? imgemessageitem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}

