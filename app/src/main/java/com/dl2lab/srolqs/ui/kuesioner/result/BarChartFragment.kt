import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dl2lab.srolqs.R
import com.github.mikephil.charting.charts.HorizontalBarChart  // Use HorizontalBarChart instead of BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class BarChartFragment : Fragment() {

    private lateinit var horizontalBarChart: HorizontalBarChart  // Change to HorizontalBarChart

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
            updateChartData(scores.toList())
        }
    }

    private fun updateChartData(scores: List<Float>) {
        // Create BarEntries with proper indices for horizontal layout
        val entries = scores.mapIndexed { index, score -> BarEntry(index.toFloat(), score) }
        val barDataSet = BarDataSet(entries, "SRL Skills")

        // Set custom color for the bars
        barDataSet.color = Color.parseColor("#1ABC9C")  // Use teal color as in the example
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 12f

        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f  // Adjust bar width

        horizontalBarChart.data = barData
        horizontalBarChart.description.isEnabled = false  // Hide description

        // Customize the XAxis (now it shows scores)
        horizontalBarChart.xAxis.apply {
            axisMinimum = 0f
            axisMaximum = 6f  // Set maximum value to 6 for scores range
            granularity = 1f
            setDrawGridLines(true)
            position = XAxis.XAxisPosition.BOTTOM  // Position labels at bottom (horizontal layout)
            textColor = Color.DKGRAY
            textSize = 12f
            typeface = android.graphics.Typeface.DEFAULT_BOLD  // Make labels bold
        }

        // Customize the Left YAxis (now it shows labels)
        val labels = listOf("Goal Settings", "Environment Structuring", "Task Strategies", "Time Management", "Help Seeking", "Self Evaluation")
        horizontalBarChart.axisLeft.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            textColor = Color.DKGRAY
            textSize = 12f
            typeface = android.graphics.Typeface.DEFAULT_BOLD  // Make labels bold
            granularity = 1f
            labelCount = labels.size
            setDrawGridLines(false)  // Disable grid lines on Y-axis for cleaner look
        }

        // Disable right Y-axis as it's not needed in this case.
        horizontalBarChart.axisRight.isEnabled = false

        // Refresh and animate the chart for better user experience.
        horizontalBarChart.invalidate()
        horizontalBarChart.animateY(2000)  // Animate vertically since it's now horizontal.
    }
}