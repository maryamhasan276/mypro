package com.example.projectcloud.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectcloud.R
import com.example.projectcloud.model.course
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_course_item.view.*

class CoursesAdapter (val context: Context, var count: Int? = null, val data: ArrayList<course>) :
    RecyclerView.Adapter<CoursesAdapter.ViewHolder>() {

    private var listener: SetClickListener? = null
    private lateinit var analytics: FirebaseAnalytics

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courseImage: ImageView = itemView.course_image
        var courseName: TextView = itemView.course_name
        var courseInstructor: TextView = itemView.course_instructor
        var courseCard: CardView = itemView.cv_courses
        var btnAdd: ImageButton = itemView.btn_add_course_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_course_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        analytics = FirebaseAnalytics.getInstance(context)
        val bundle = Bundle()

        if (count != null) {
            if (count!! >= 5) {
                holder.btnAdd.isEnabled = false
            }
        }

        holder.courseName.text = data[position].course_name
        holder.courseInstructor.text = data[position].course_instructor
        Glide.with(context).load(data[position].course_image).into(holder.courseImage)

        holder.btnAdd.setOnClickListener {
            listener?.onButtonClickListener(
                position,
                data[position]
            )
        }

        holder.courseCard.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: clicked")
            listener?.onItemClickListener(position, data[position])
            listener?.onItemClickListener(position, data[position])
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, data[position].course_name)
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)
        }

        Glide.with(context).load(data[position].course_image).circleCrop().into(holder.courseImage)
    }

    public fun setListener(listener: SetClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = data.size

    interface SetClickListener {
        fun onItemClickListener(position: Int, course: course)
        fun onButtonClickListener(position: Int, course: course)
    }
}
