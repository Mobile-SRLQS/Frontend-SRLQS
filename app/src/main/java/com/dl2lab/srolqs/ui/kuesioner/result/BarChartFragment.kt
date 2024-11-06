package com.dl2lab.srolqs.ui.kuesioner.result

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dl2lab.srolqs.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate


class BarChartFragment : Fragment() {

    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bar_chart, container, false)
        barChart = view.findViewById(R.id.bar_chart_fragment)
        return view
    }

    fun updateChartData(scores: List<Float>) {
        val entries = scores.mapIndexed { index, score -> BarEntry(index.toFloat(), score) }
        val barDataSet = BarDataSet(entries, "SRL Skills")
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 12f

        barChart.data = BarData(barDataSet)
        barChart.description.text = "Bar Chart"
        barChart.invalidate()  // Refresh chart
        barChart.animateY(2000)
    }
}
