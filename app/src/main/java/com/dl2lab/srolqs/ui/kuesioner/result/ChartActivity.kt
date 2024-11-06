package com.dl2lab.srolqs.ui.kuesioner.result

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

    private val viewModel: QuestionnaireViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        // Initialize buttons and set click listeners
        findViewById<Button>(R.id.btn_show_radar_chart).setOnClickListener {
            viewModel.scoreResult.value?.let { scores ->
                displayFragment(RadarChartFragment().apply {
                    arguments = Bundle().apply { putFloatArray("SCORES", scores.toFloatArray()) }
                })
            }
        }

        findViewById<Button>(R.id.btn_show_bar_chart).setOnClickListener {
            viewModel.scoreResult.value?.let { scores ->
                displayFragment(BarChartFragment().apply {
                    arguments = Bundle().apply { putFloatArray("SCORES", scores.toFloatArray()) }
                })
            }
        }

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
}
