package com.dl2lab.srolqs.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.PeriodDataItem
import com.dl2lab.srolqs.databinding.ItemPeriodBinding
import com.dl2lab.srolqs.ui.kuesioner.question.QuestionnaireQuestionActivity
import com.dl2lab.srolqs.ui.kuesioner.result.ChartActivity

class PeriodAdapter(
    private val periodList: List<PeriodDataItem?>,
    private val itemClickListener: OnPeriodItemClickListener
) : RecyclerView.Adapter<PeriodAdapter.PeriodViewHolder>() {

    inner class PeriodViewHolder(private val binding: ItemPeriodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(periodItem: PeriodDataItem) {
            binding.periodTitle.text = periodItem.title
            binding.periodDate.text = periodItem.subtitle
            var buttonText = "Mulai Kerjakan"

            if (periodItem.isDone == true) {
                buttonText = "Lihat Hasil"
                binding.periodButton.setOnClickListener{
                    seeGraphic(binding.periodButton.context, periodItem)
                }
            } else{
                binding.periodButton.setOnClickListener {
                    doQuestionnaire(binding.periodButton.context, periodItem)
                }
            }
            if (periodItem.isAvailable == false) {
                buttonText = "Belum Tersedia"
                binding.periodButton.setBackgroundColor(
                    ContextCompat.getColor(binding.periodButton.context, R.color.grey)
                )
                binding.periodButton.isEnabled = false
            }
            binding.periodButton.text = buttonText

        }
    }

    private fun doQuestionnaire(context: Context, periodItem: PeriodDataItem) {
        val intent = Intent(context, QuestionnaireQuestionActivity::class.java)
        intent.putExtra("classId", periodItem.classId)
        intent.putExtra("period", periodItem.periodName)
        context.startActivity(intent)
    }

    private fun seeGraphic(context: Context, periodItem: PeriodDataItem) {
        val intent = Intent(context, ChartActivity::class.java)
        intent.putExtra("CLASSID", periodItem.classId)
        intent.putExtra("PERIOD", periodItem.periodName.toString())
        context.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodViewHolder {
        val binding = ItemPeriodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeriodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeriodViewHolder, position: Int) {
        periodList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = periodList.size
}
