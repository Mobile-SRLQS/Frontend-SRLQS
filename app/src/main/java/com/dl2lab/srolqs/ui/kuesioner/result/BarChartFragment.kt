import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dl2lab.srolqs.R
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class BarChartFragment : Fragment() {

    private lateinit var horizontalBarChart: HorizontalBarChart


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bar_chart, container, false)
        horizontalBarChart = view.findViewById(R.id.bar_chart_fragment)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var scores1 = arguments?.getFloatArray("SCORES")?.toList()
        var scores2 = arguments?.getFloatArray("SCORES2")?.toList()
        if (scores1 != null) {
            updateChartData(scores1.toList())
        }
        if (scores1 != null && scores2 != null) {
            updateChartData(scores1.toList(), scores2.toList())
        }

    }

    private fun updateChartData(scores1: List<Float>, scores2: List<Float>? = null) {
        val entries1 = scores1.mapIndexed { index, score -> BarEntry(index.toFloat(), score) }
        val barDataSet1 = BarDataSet(entries1, "Hasil SRL Anda")
        barDataSet1.color = Color.parseColor("#1ABC9C")
        barDataSet1.valueTextColor = Color.BLACK
        barDataSet1.valueTextSize = 10f
        barDataSet1.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when {
                    value % 1 == 0f -> value.toInt().toString()
                    else -> String.format("%.2f", value)
                }
            }
        }

        val barData = if (scores2 != null) {
            val entries2 = scores2.mapIndexed { index, score -> BarEntry(index.toFloat(), score) }
            val barDataSet2 = BarDataSet(entries2, "Rata-rata SRL sekelas")
            barDataSet2.color = Color.parseColor("#FF5733")
            barDataSet2.valueTextColor = Color.BLACK
            barDataSet2.valueTextSize = 10f
            barDataSet2.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when {
                        value % 1 == 0f -> value.toInt().toString()
                        else -> String.format("%.2f", value)
                    }
                }
            }
            BarData(barDataSet1, barDataSet2)
        } else {
            BarData(barDataSet1)
        }

        barData.barWidth = 0.3f
        horizontalBarChart.data = barData
        horizontalBarChart.description.isEnabled = false

        horizontalBarChart.apply {
            setExtraOffsets(30f, 30f, 30f, 30f) // left, top, right, bottom
        }

        val labels = listOf(
            "Goal Settings",
            "Environment Structuring",
            "Task Strategies",
            "Time Management",
            "Help Seeking",
            "Self Evaluation"
        )
        horizontalBarChart.xAxis.apply {
            labelCount = labels.size
            valueFormatter = IndexAxisValueFormatter(labels)
            axisMinimum = -0.5f // Tambahkan space di awal
            axisMaximum = labels.size - 0.5f // Tambahkan space di akhir
            granularity = 1f
            setDrawGridLines(true)
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.DKGRAY
            textSize = 8f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        horizontalBarChart.axisLeft.apply {
            textColor = Color.DKGRAY
            textSize = 10f // Kurangi ukuran text
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            granularity = 1f
            setDrawGridLines(false)
            axisMinimum = 0f
            axisMaximum = 6f
            setDrawLabels(true) // Pastikan label ditampilkan
            setLabelCount(6, true) // Sesuaikan jumlah label
        }


//        horizontalBarChart.legend.apply {
//            yOffset = 15f
//            xOffset = 15f
//        }

        if (scores2 != null) {
            barData.barWidth = 0.15f // Kurangi lebar bar
            horizontalBarChart.groupBars(
                -0.45f, // fromX - sesuaikan nilai ini untuk mengatur posisi horizontal
                0.65f,  // groupSpace
                0.02f  // barSpace
            )
        }

        horizontalBarChart.axisRight.isEnabled = false
        horizontalBarChart.axisRight.apply {
            axisMinimum = 0f
            axisMaximum = 6f
            granularity = 1f
            setDrawGridLines(false)
            setDrawLabels(false)
        }
        horizontalBarChart.apply {
            setScaleEnabled(true)
            isDragEnabled = true
            setVisibleXRange(0f, 6f)
        }


        horizontalBarChart.invalidate()
        horizontalBarChart.animateY(2000)
    }
}

