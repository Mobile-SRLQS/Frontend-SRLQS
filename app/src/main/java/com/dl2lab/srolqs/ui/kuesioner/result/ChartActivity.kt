package com.dl2lab.srolqs.ui.kuesioner.result

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        // Set initial fragment (Radar Chart)
        supportFragmentManager.beginTransaction()
            .replace(R.id.chart_fragment_container, RadarChartFragment())
            .commit()

        // Button to show Radar Chart
        findViewById<Button>(R.id.btn_show_radar_chart).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.chart_fragment_container, RadarChartFragment())
                .commit()
        }

        // Button to show Bar Chart
        findViewById<Button>(R.id.btn_show_bar_chart).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.chart_fragment_container, BarChartFragment())
                .commit()
        }

        // Fetch data for the specified class ID and period
        val classId = intent.getStringExtra("CLASSID")
        val period = intent.getStringExtra("PERIOD")
        if (classId != null && period != null) {
            Log.d("ChartActivity", "Fetching score result for classId: $classId, period: $period")
            viewModel.fetchScoreResult(classId, period)
        } else {
            Log.e("ChartActivity", "classId or period is null.")
        }

        // Observe scoreResult data and populate charts when available
        viewModel.scoreResult.observe(this) { scores ->
            Log.d("ChartActivity", "Observed scoreResult data: $scores")
            val currentFragment = supportFragmentManager.findFragmentById(R.id.chart_fragment_container)
            if (currentFragment is RadarChartFragment) {
                currentFragment.updateChartData(scores)
            } else if (currentFragment is BarChartFragment) {
                currentFragment.updateChartData(scores)
            }
        }
    }
}
