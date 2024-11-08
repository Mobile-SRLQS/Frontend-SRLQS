package com.dl2lab.srolqs.ui.kuesioner.result

import BarChartFragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class ChartActivity : AppCompatActivity() {
    private lateinit var toggleChartButton: Button
    private var isRadarChartDisplayed = true


    private val viewModel: QuestionnaireViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        toggleChartButton = findViewById(R.id.btn_toggle_chart)
        toggleChartButton.setOnClickListener { toggleChart() }


        // Fetch data for the specified class ID and period
        val classId = intent.getStringExtra("CLASSID")
        val period = intent.getStringExtra("PERIOD")
        if (classId != null && period != null) {
            viewModel.fetchScoreResult(classId, period)
        }

        // Observe scoreResult data and set initial fragment when data is available
        viewModel.scoreResult.observe(this) { scores ->
            if (scores != null && scores.isNotEmpty()) {
                displayFragment(RadarChartFragment().apply {
                    arguments = Bundle().apply { putFloatArray("SCORES", scores.toFloatArray()) }
                })
            } else {
                Log.e("ChartActivity", "No scores available to display.")
            }
        }
    }

    // Helper function to display the given fragment
    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.chart_fragment_container, fragment)
            .commit()
    }

    private fun toggleChart() {
        viewModel.scoreResult.value?.let { scores ->
            if (isRadarChartDisplayed) {
                displayBarChart(scores.toFloatArray())
                toggleChartButton.text = "Lihat Versi Radar Chart"
            } else {
                displayRadarChart(scores.toFloatArray())
                toggleChartButton.text = "Lihat Versi Bar Chart"
            }
            isRadarChartDisplayed = !isRadarChartDisplayed
        }
    }

    private fun displayRadarChart(scores: FloatArray) {
        displayFragment(RadarChartFragment().apply {
            arguments = Bundle().apply { putFloatArray("SCORES", scores) }
        })
    }

    private fun displayBarChart(scores: FloatArray) {
        displayFragment(BarChartFragment().apply {
            arguments = Bundle().apply { putFloatArray("SCORES", scores) }
        })
    }
}
