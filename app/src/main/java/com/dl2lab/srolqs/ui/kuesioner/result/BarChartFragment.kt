import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dl2lab.srolqs.R
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class BarChartFragment : Fragment() {

    private lateinit var horizontalBarChart: HorizontalBarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bar_chart, container, false)
        horizontalBarChart = view.findViewById(R.id.bar_chart_fragment)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getFloatArray("SCORES")?.let { scores ->
            arguments?.getFloatArray("SCORES2")?.let { scores2 ->
                updateChartData(scores.toList(), scores2.toList())
            }
        }
    }

    private fun updateChartData(scores1: List<Float>, scores2: List<Float>) {
        // Create BarEntries for both datasets
        val entries1 = scores1.mapIndexed { index, score -> BarEntry(index.toFloat(), score) }
        val entries2 = scores2.mapIndexed { index, score -> BarEntry(index.toFloat(), score) }



        val barDataSet1 = BarDataSet(entries1, "SRL Skills 1")
        barDataSet1.color = Color.parseColor("#1ABC9C")
        barDataSet1.valueTextColor = Color.BLACK
        barDataSet1.valueTextSize = 12f
        barDataSet1.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when {
                    value % 1 == 0f -> value.toInt().toString()
                    else -> String.format("%.1f", value)
                }
            }
        }

        val barDataSet2 = BarDataSet(entries2, "SRL Skills 2")
        barDataSet2.color = Color.parseColor("#FF5733")
        barDataSet2.valueTextColor = Color.BLACK
        barDataSet2.valueTextSize = 12f
        barDataSet2.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when {
                    value % 1 == 0f -> value.toInt().toString()
                    else -> String.format("%.1f", value)
                }
            }
        }

        val barData = BarData(barDataSet1, barDataSet2)
        barData.barWidth = 0.3f  // Adjust bar width

        horizontalBarChart.data = barData
        horizontalBarChart.description.isEnabled = false

        // Customize the XAxis (now it shows scores)
        val labels = listOf("","Goal Settings", "Environment Structuring", "Task Strategies", "Time Management", "Help Seeking", "Self Evaluation")


        horizontalBarChart.xAxis.apply {
            labelCount = labels.size - 1
            valueFormatter = IndexAxisValueFormatter(labels)
            axisMinimum = 0f
            axisMaximum = scores1.size.toFloat() + 0.5f // Tambahkan ruang ekstra untuk grup terakhir agar terlihat jelas.
            granularity = 1f
            setDrawGridLines(true)
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.DKGRAY
            textSize = 6f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }


        horizontalBarChart.axisLeft.apply {
            textColor = Color.DKGRAY
            textSize = 12f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            granularity = 1f
            setDrawGridLines(false)
            axisMinimum = -0.5f
            axisMaximum = labels.size - 0.5f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when {
                        value % 1 == 0f -> value.toInt().toString()
                        else -> String.format("%.1f", value)
                    }
                }
            }
        }


        horizontalBarChart.axisRight.isEnabled = false


        horizontalBarChart.axisRight.apply {
            axisMaximum = 6f
            axisMinimum = 0f
        }

        horizontalBarChart.axisLeft.apply {
            axisMaximum = 6f
            axisMinimum = 0f
        }


        horizontalBarChart.groupBars(0f, 0.4f, 0.05f)

        horizontalBarChart.invalidate()
        horizontalBarChart.animateY(2000)
    }
}