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

        if (item.isExpanded) {
            holder.tvAnswer.visibility = View.VISIBLE
            holder.ivExpandIcon.setImageResource(R.drawable.ic_chevron_up)
        } else {
            holder.tvAnswer.visibility = View.GONE
            holder.ivExpandIcon.setImageResource(R.drawable.ic_chevron_down)
        }
    }

    override fun getItemCount(): Int = faqList.size
}
