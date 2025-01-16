import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dl2lab.srolqs.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.components.XAxis

class LineChartFragment : Fragment() {
    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_line_chart, container, false)
        lineChart = view.findViewById(R.id.lineChart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scores = arguments?.getFloatArray("SCORES")?.toList() ?: emptyList()
        if (scores.isNotEmpty()) {
            updateChartData(scores)
        }
    }

    private fun updateChartData(scores: List<Float>) {
        val entries = scores.mapIndexed { index, score -> Entry(index.toFloat(), score) }
        val lineDataSet = LineDataSet(entries, "Keterampilan SRL Anda").apply {
            color = Color.parseColor("#1ABC9C")
            valueTextColor = Color.BLACK
            valueTextSize = 12f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when {
                        value % 1 == 0f -> value.toInt().toString()
                        else -> String.format("%.2f", value)
                    }
                }
            }
        }

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.description = Description().apply { isEnabled = false }
        val xAxisFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value.toInt()) {
                    0 -> "Periode 1"
                    1 -> "Periode 2"
                    2 -> "Periode 3"
                    else -> ""
                }
            }
        }
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(true)
            textColor = Color.DKGRAY
            textSize = 12f
            granularity = 1f
            axisMinimum = 0f
            axisMaximum = scores.size.toFloat() - 1
            valueFormatter = xAxisFormatter
        }

        lineChart.apply {
            setExtraOffsets(20f, 10f, 30f, 20f)
        }


        lineChart.axisLeft.apply {
            textColor = Color.DKGRAY
            textSize = 12f
            granularity = 1f
            axisMinimum = 0f
            setDrawGridLines(false)
        }

        lineChart.axisRight.isEnabled = false
        lineChart.invalidate()
        lineChart.animateY(2000)
    }
}