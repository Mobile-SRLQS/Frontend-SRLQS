package com.dl2lab.srolqs.ui.kuesioner.result

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.viewmodel.LoginViewModel
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

        // Observe scoreResult data and populate charts when available
        viewModel.scoreResult.observe(this) { scores ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.chart_fragment_container)
            if (currentFragment is RadarChartFragment) {
                currentFragment.updateChartData(scores)
            } else if (currentFragment is BarChartFragment) {
                currentFragment.updateChartData(scores)
            }
        }

        // Fetch data for the specified class ID and period
        val classId = intent.getStringExtra("CLASSID")
        val periode = intent.getStringExtra("PERIODE")
        if (classId != null) {
            if (periode != null) {
                viewModel.fetchScoreResult(classId, periode)
            }
        }
    }
}
