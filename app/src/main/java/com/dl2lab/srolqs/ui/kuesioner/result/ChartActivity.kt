package com.dl2lab.srolqs.ui.kuesioner.result

import BarChartFragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityChartBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class ChartActivity : AppCompatActivity() {
    private lateinit var toggleChartButton: Button
    private lateinit var toggleAverageDataButton: Button
    private var isRadarChartDisplayed = true
    private var isAvgDataRadarChartDisplayed = false
    private lateinit var binding: ActivityChartBinding

    private val viewModel: QuestionnaireViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toggleChartButton = findViewById(R.id.btn_toggle_chart)
        toggleAverageDataButton = findViewById(R.id.btn_toggle_average_data)
        toggleChartButton.setOnClickListener { toggleChart() }
        toggleAverageDataButton.setOnClickListener { toggleAverageDataRadarChart() }

        addReccomendation()
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

    private fun addReccomendation() {
        viewModel.reccomendationText.observe(this) { reccomendationText ->
            if (reccomendationText != null) {
                val reccomendationTextView = findViewById<TextView>(R.id.chart_description)
                reccomendationTextView.text = reccomendationText
            } else {
                Log.e("ChartActivity", "No reccomendation available to display.")
            }
        }
        viewModel.reccomendation.observe(this) { reccomendation ->
            if (reccomendation != null) {
                binding.GSTitle.text = reccomendation.gsTitle
                binding.HSTitle.text = reccomendation.hsTitle
                binding.ESTitle.text = reccomendation.esTitle
                binding.SETitle.text = reccomendation.seTitle
                binding.TMTitle.text = reccomendation.tmTitle
                binding.TSTitle.text = reccomendation.tsTitle
                binding.GSSubtitle.text = reccomendation.gsReccomendation
                binding.HSSubtitle.text = reccomendation.hsReccomendation
                binding.ESSubtitle.text = reccomendation.esReccomendation
                binding.SESubtitle.text = reccomendation.seReccomendation
                binding.TMSubtitle.text = reccomendation.tmReccomendation
                binding.TSSubtitle.text = reccomendation.tsReccomendation
            } else {
                Log.e("ChartActivity", "No reccomendation available to display.")
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
                isAvgDataRadarChartDisplayed = false

            } else {
                displayRadarChart(scores.toFloatArray())
                toggleChartButton.text = "Lihat Versi Bar Chart"
                isAvgDataRadarChartDisplayed = false
            }
            isRadarChartDisplayed = !isRadarChartDisplayed
        }
    }

    private fun toggleAverageDataRadarChart() {
        viewModel.scoreResult.value?.let { scores ->
            if (isAvgDataRadarChartDisplayed) {
                displayRadarChart(scores.toFloatArray())
                toggleAverageDataButton.text = "Lihat Rata-Rata Kelas"
            } else {
                viewModel.scoreAverage.value?.let { averageScores ->
                    displayFragment(RadarChartFragment().apply {
                        arguments = Bundle().apply {
                            putFloatArray("SCORES", scores.toFloatArray())
                            putFloatArray("SCORES2", averageScores.toFloatArray())
                        }
                    })
                }
                toggleAverageDataButton.text = "Tanpa Rata-Rata Kelas"
            }
            isAvgDataRadarChartDisplayed = !isAvgDataRadarChartDisplayed
        }
    }

    private fun displayRadarChart(scores: FloatArray) {
        displayFragment(RadarChartFragment().apply {
            arguments = Bundle().apply { putFloatArray("SCORES", scores) }
        })
    }

    private fun displayBarChart(scores: FloatArray) {
        displayFragment(BarChartFragment().apply {
            arguments = Bundle().apply {
                putFloatArray("SCORES", scores)
                putFloatArray("SCORES2", scores)
            }
        })
    }


}