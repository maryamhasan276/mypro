package com.example.projectcloud.model

import android.content.Context
import com.example.projectcloud.R
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_item_text_message.view.*

class textmessageitem (
    val message: textmessage,
    val context: Context
) :
    messageitem(message) {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_message_text.text = message.text
        super.bind(viewHolder, position)
    }

    override fun getLayout() = R.layout.activity_item_text_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is textmessageitem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? textmessageitem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}
