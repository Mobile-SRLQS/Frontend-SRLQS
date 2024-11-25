package com.dl2lab.srolqs.ui.kuesioner.result

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dl2lab.srolqs.R
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class RadarChartFragment : Fragment() {

    private lateinit var radarChart: RadarChart
    private val lightGreen = Color.parseColor("#1ABC9C")  // Define the custom teal color


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_radar_chart, container, false)
        radarChart = view.findViewById(R.id.radar_chart_fragment)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var scores = arguments?.getFloatArray("SCORES")
        var scores2 = arguments?.getFloatArray("SCORES2")
        if (scores != null) {
            updateChartData(scores.toList())
        }
        if (scores!=null && scores2 != null) {
            updateChartData(scores.toList(), scores2.toList())
        }


    }

    private fun updateChartData(scores1: List<Float>, scores2: List<Float>? = null) {
        val entries1 = scores1.mapIndexed { index, score -> RadarEntry(score) }
        val radarDataSet1 = RadarDataSet(entries1, "Keterampilan SRL Anda")
        radarDataSet1.lineWidth = 2f
        radarDataSet1.valueTextColor = Color.BLACK
        radarDataSet1.valueTextSize = 12f
        radarDataSet1.color = Color.parseColor("#1ABC9C")
        radarDataSet1.fillColor = Color.parseColor("#1ABC9C")
        radarDataSet1.setDrawFilled(true)
        radarDataSet1.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when {
                    value % 1 == 0f -> value.toInt().toString()
                    else -> String.format("%.1f", value)
                }
            }
        }

        val radarData = if (scores2 != null) {
            val entries2 = scores2.mapIndexed { index, score -> RadarEntry(score) }
            val radarDataSet2 = RadarDataSet(entries2, "Rata-rata Keteramplilan SRL Kelas Anda")
            radarDataSet2.setColors(ColorTemplate.MATERIAL_COLORS, 255)
            radarDataSet2.lineWidth = 2f
            radarDataSet2.valueTextColor = Color.BLACK
            radarDataSet2.valueTextSize = 12f
            radarDataSet2.color = Color.RED
            radarDataSet2.fillColor = Color.RED
            radarDataSet2.setDrawFilled(true)
            radarDataSet2.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when {
                        value % 1 == 0f -> value.toInt().toString()
                        else -> String.format("%.1f", value)
                    }
                }
            }
            RadarData(radarDataSet1, radarDataSet2)
        } else {
            RadarData(radarDataSet1)
        }

        radarChart.data = radarData
        radarChart.description.text = "Radar Chart"

        val labels = listOf("Goal Settings", "Environment Structuring", "Task Strategies", "Time Management", "Help Seeking", "Self Evaluation")

        radarChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            textColor = Color.BLACK
            textSize = 6f
            typeface = Typeface.DEFAULT_BOLD
        }

        radarChart.yAxis.apply {
            axisMaximum = 6f
            axisMinimum = 0f
            granularity = 1f
            isGranularityEnabled = true
            setDrawLabels(true)
            setLabelCount(7, true)
            textColor = Color.DKGRAY
            textSize = 12f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when {
                        value % 1 == 0f -> value.toInt().toString()
                        else -> String.format("%.1f", value)
                    }
                }
            }
        }

        radarChart.description.isEnabled = false
        radarChart.legend.isEnabled = true
        radarChart.invalidate()
        radarChart.animateY(2000)
    }
}
