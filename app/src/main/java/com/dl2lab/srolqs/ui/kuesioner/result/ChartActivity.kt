package com.dl2lab.srolqs.ui.kuesioner.result

import BarChartFragment
import LineChartFragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityChartBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel
import java.util.Locale

class ChartActivity : AppCompatActivity() {
    private lateinit var toggleChartButton: Button
    private lateinit var periodSpinner: Spinner
    private lateinit var toggleAverageDataButton: Button
    private var isRadarChartDisplayed = true
    private var isAvgDataRadarChartDisplayed = false
    private var isAvgDataBarChartDisplayed = false
    private var classId: String? = null
    private var period: String? = null
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
        periodSpinner = findViewById(R.id.spinner_dropdown)
        toggleChartButton.setOnClickListener { toggleChart() }
        toggleAverageDataButton.setOnClickListener { toggleAverageData() }
        LoadingManager.init(this)
        addReccomendation()
        setupPeriodSpinner()
        setupDimensionSpinner()
        // Fetch data for the specified class ID and period
        classId = intent.getStringExtra("CLASSID")
        period = intent.getStringExtra("PERIOD")
        if (classId != null && period != null) {
            fetchDataForPeriod(period!!)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                LoadingManager.show()
            } else {
                LoadingManager.hide()
            }
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
        binding.btnBack.setOnClickListener{
            finish()
        }
    }


    private fun showPeriodData(isShow: Boolean) {
        if (isShow) {
            binding.scrollView.visibility = View.VISIBLE
            binding.btnToggleAverageData.visibility = View.VISIBLE
            binding.btnToggleChart.visibility = View.VISIBLE
            binding.spinnerDimensionLinearLayout.visibility = View.INVISIBLE
            binding.tvDimension.visibility = View.INVISIBLE
        } else {
            binding.scrollView.visibility = View.INVISIBLE
            binding.btnToggleAverageData.visibility = View.INVISIBLE
            binding.btnToggleChart.visibility = View.INVISIBLE
            binding.spinnerDimensionLinearLayout.visibility = View.VISIBLE
            binding.tvDimension.visibility = View.VISIBLE
        }
    }

    private fun setupPeriodSpinner() {
        classId = intent.getStringExtra("CLASSID")
        // Ambil period dari intent
        val intentPeriod = intent.getStringExtra("PERIOD")?.toIntOrNull() ?: 1

        var periodString = arrayOf("")
        classId?.let { viewModel.fetchAvailablePeriod(it) }
        viewModel.periods.observe(this) { periods ->
            periodString = periods.toTypedArray()
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, periodString)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            periodSpinner.adapter = adapter

            // Set selection berdasarkan period dari intent
            // Karena array dimulai dari 0, kurangi intentPeriod dengan 1
            val selectionIndex = intentPeriod - 1
            if (selectionIndex in periodString.indices) {
                periodSpinner.setSelection(selectionIndex)
            }
        }

        periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedPeriod = periodString[position]
                if (selectedPeriod != period) {
                    period = selectedPeriod
                    if (selectedPeriod != "Progress Anda") {
                        // Gunakan position + 1 untuk mendapatkan nomor period yang benar
                        fetchDataForPeriod((position + 1).toString())
                    } else {
                        fetchDataForPeriod("Progress Anda")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun setupDimensionSpinner() {
        val dimensions = arrayOf(
            "Goal Setting",
            "Environment Structuring",
            "Task Strategies",
            "Time Management",
            "Help Seeking",
            "Self Evaluation"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dimensions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        var dimensionSpinner = findViewById<Spinner>(R.id.spinner_dimension)
        dimensionSpinner.adapter = adapter

        dimensionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedPeriod = dimensions[position]
                val selectedDimension = selectedPeriod.lowercase(Locale.getDefault()).replace(" ", "_")
                fetchDataForDimension(selectedDimension)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                fetchDataForDimension("goal_setting")
            }
        }

        // Set initial selection
        val initialPeriodIndex = dimensions.indexOf("Goal Setting")
        if (initialPeriodIndex != -1) {
            dimensionSpinner.setSelection(initialPeriodIndex)
        }
    }

    private fun fetchDataForDimension(selectedDimension: String) {
        classId?.let {
            viewModel.fetchStudentProgress(it, selectedDimension)
        }
        viewModel.studentProgress.observe(this) { progress ->
            Log.d("ChartActivity", "Progress data received: $progress") // Log the fetched progress

            if (progress != null) {
                binding.tvDimension.text = progress.description // Ensure this is updating the UI
                val scores = progress.scores ?: emptyList()
                Log.d("ChartActivity", "Scores for $selectedDimension: $scores") // Log scores

                if (scores.isNotEmpty()) {
                    val floatScores = scores.map { score -> score?.toFloat() ?: 0f }.toFloatArray()
                    displayFragment(LineChartFragment().apply {
                        arguments = Bundle().apply {
                            putFloatArray("SCORES", floatScores)
                        }
                    })
                } else {
                    Log.e("ChartActivity", "No scores available for the selected dimension.")
                }
            } else {
                Log.e("ChartActivity", "Progress data is null, ensure data is fetched correctly.")
            }
        }
    }


    private fun fetchDataForPeriod(selectedPeriod: String) {
        if (classId != null) {
            if (selectedPeriod != "Progress Anda") {
                viewModel.fetchScoreResult(classId!!, selectedPeriod)

                showPeriodData(true)
            } else {
                viewModel.fetchStudentProgress(classId!!, "goal_setting")
                fetchDataForDimension("goal_setting")
                showPeriodData(false)
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
        supportFragmentManager.beginTransaction().replace(R.id.chart_fragment_container, fragment)
            .commit()
    }

    private fun toggleChart() {
        isAvgDataBarChartDisplayed = false
        isAvgDataRadarChartDisplayed = false
        toggleAverageDataButton.text = "Dengan Rata-Rata Kelas"
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

    private fun toggleAverageData() {
        if (isAvgDataRadarChartDisplayed || isAvgDataBarChartDisplayed) {
            toggleAverageDataButton.text = "Dengan Rata-Rata Kelas"
        } else {
            toggleAverageDataButton.text = "Tanpa Rata-Rata Kelas"
        }

        if (isRadarChartDisplayed) {
            toggleAverageDataRadarChart()
            isAvgDataRadarChartDisplayed = !isAvgDataRadarChartDisplayed
            isAvgDataBarChartDisplayed = false
        } else {
            toggleAverageDataBarChart()
            isAvgDataBarChartDisplayed = !isAvgDataBarChartDisplayed
            isAvgDataRadarChartDisplayed = false
        }
    }

    private fun toggleAverageDataRadarChart() {
        viewModel.scoreResult.value?.let { scores ->
            if (isAvgDataRadarChartDisplayed) {
                displayRadarChart(scores.toFloatArray())
            } else {
                viewModel.scoreAverage.value?.let { averageScores ->
                    displayFragment(RadarChartFragment().apply {
                        arguments = Bundle().apply {
                            putFloatArray("SCORES", scores.toFloatArray())
                            putFloatArray("SCORES2", averageScores.toFloatArray())
                        }
                    })
                }
            }
        }
    }

    private fun toggleAverageDataBarChart() {
        viewModel.scoreResult.value?.let { scores ->
            if (isAvgDataBarChartDisplayed) {
                displayBarChart(scores.toFloatArray())
            } else {
                viewModel.scoreAverage.value?.let { averageScores ->
                    displayFragment(BarChartFragment().apply {
                        arguments = Bundle().apply {
                            putFloatArray("SCORES", scores.toFloatArray())
                            putFloatArray("SCORES2", averageScores.toFloatArray())
                        }
                    })
                }
            }
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
            }
        })
    }
}