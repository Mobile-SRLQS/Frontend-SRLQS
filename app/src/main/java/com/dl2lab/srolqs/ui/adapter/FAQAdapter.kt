package com.dl2lab.srolqs.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.preference.FAQItem

class FAQAdapter(private val faqList: List<FAQItem>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    inner class FAQViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQuestion: TextView = view.findViewById(R.id.tvQuestion)
        val tvAnswer: TextView = view.findViewById(R.id.tvAnswer)
        val ivExpandIcon: ImageView = view.findViewById(R.id.ivExpandIcon)

        init {
            // Set click listener for the entire item view
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = faqList[position]
                    item.isExpanded = !item.isExpanded
                    notifyItemChanged(position)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val item = faqList[position]
        holder.tvQuestion.text = item.question
        holder.tvAnswer.text = item.answer

        // Handle visibility and arrow icon rotation
        if (item.isExpanded) {
            holder.tvAnswer.visibility = View.VISIBLE
            holder.ivExpandIcon.setImageResource(R.drawable.ic_arrow_up) // Icon for expanded state
        } else {
            holder.tvAnswer.visibility = View.GONE
            holder.ivExpandIcon.setImageResource(R.drawable.ic_arrow_down) // Icon for collapsed state
        }
    }

    override fun getItemCount(): Int = faqList.size
}
