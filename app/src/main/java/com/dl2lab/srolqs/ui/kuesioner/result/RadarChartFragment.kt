package com.dl2lab.srolqs.ui.kuesioner.result

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dl2lab.srolqs.R
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class RadarChartFragment : Fragment() {

    private lateinit var radarChart: RadarChart

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

        radarChart.data = RadarData(radarDataSet)
        radarChart.description.text = "Radar Chart"
        radarChart.invalidate()
        radarChart.animateY(2000)
    }
}
