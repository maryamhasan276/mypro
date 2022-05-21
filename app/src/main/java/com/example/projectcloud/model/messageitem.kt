package com.example.projectcloud.model

import android.view.Gravity
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectcloud.R
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.activity_item_text_message.view.*
import org.jetbrains.anko.backgroundResource

abstract class messageitem (private val message: Message) :
    Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: RecyclerView.ViewHolder) {
        val dateFormat = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)


        viewHolder.itemView.textView_message_time.text = dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(viewHolder: ViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.itemView.message_root.apply {


                backgroundResource = R.drawable.whiite
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }
        } else {
            viewHolder.itemView.message_root.apply {
                backgroundResource = R.drawable.primary
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }
}
