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
        arguments?.getFloatArray("SCORES")?.let { scores ->
            updateChartData(scores.toList())
        }
    }

    private fun updateChartData(scores: List<Float>) {
        val entries = scores.mapIndexed { index, score -> RadarEntry(score) }
        val radarDataSet = RadarDataSet(entries, "SRL Skills")


        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        radarDataSet.lineWidth = 2f
        radarDataSet.valueTextColor = Color.BLACK
        radarDataSet.valueTextSize = 12f

        // Apply custom color for both line and filled area
        radarDataSet.color = lightGreen         // Line color
        radarDataSet.fillColor = lightGreen     // Fill color
        radarDataSet.setDrawFilled(true)         // Enable filled area with custom color

        radarChart.data = RadarData(radarDataSet)
        radarChart.description.text = "Radar Chart"

        val labels = listOf("Goal Settings", "Environment Structuring", "Task Strategies", "Time Management", "Help Seeking", "Self Evaluation")

        // Set up the XAxis to display the labels
        radarChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            textColor = Color.BLACK
            textSize = 6f
            typeface = Typeface.DEFAULT_BOLD

        }
        // Set the maximum value for the Y axis
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
                        value % 1 == 0f -> value.toInt().toString() // Show as whole number if it's a whole number
                        else -> String.format("%.1f", value) // Format to one decimal place otherwise
                    }
                }
            }

        }


        radarChart.invalidate()
        radarChart.animateY(2000)
    }
}
