package com.eeszen.reptide.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.eeszen.reptide.MainActivity
import com.eeszen.reptide.R
import com.eeszen.reptide.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels{
        HomeViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchCoroutines()

        binding.navigateWorkoutFragment.setOnClickListener {
            (requireActivity() as MainActivity).selectBottomNavItem(R.id.workoutFragment)
        }

        showChart()
    }

    // Show Chart
    private fun showChart(){
        lifecycleScope.launch {
            viewModel.completedWorkouts.collectLatest { workouts ->
                if (workouts.isNotEmpty()) {
                    setupWeightChart()
                } else {
                    binding.weightLineChart.clear()
                    binding.weightLineChart.setNoDataText("No progress data yet")
                }
            }
        }
    }

    // Home Details
    private fun launchCoroutines(){
        lifecycleScope.launch {
            viewModel.highestWeight.collectLatest { pr ->
                binding.tvHomePR.text = pr.toString()
            }
        }
        lifecycleScope.launch {
            viewModel.lastWorkoutType.collectLatest { type ->
                binding.tvHomeWorkoutType.text = type?.name ?: "NONE"
            }
        }
        lifecycleScope.launch {
            viewModel.lastWorkoutName.collectLatest { name ->
                binding.tvHomeLastWorkoutName.text = name.toString()
            }
        }
    }

    // Main Chart Logic
    private fun setupWeightChart() {
        val chart = binding.weightLineChart

        // Basic chart settings
        chartSettings(chart)

        // Progress Data
        chartEntries(chart)

        chartLayoutStyling(chart)

        // Legend
        val legend = chart.legend
        legend.isEnabled = false

        chart.animateX(1000)
        chart.invalidate()
    }

    // Chart Settings
    fun chartSettings(chart:LineChart){
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.setDrawGridBackground(false)
        chart.setNoDataText("No progress data yet")
        chart.setNoDataTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
        chart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_gray))
    }

    // Chart Data
    fun chartEntries(chart: LineChart){
        val entries = viewModel.weightEntries().mapIndexed { i, weight ->
            Entry(i.toFloat(), weight)
        }
        val dataSet = LineDataSet(entries, "Weight Progress")
        dataSet.apply {
            color = ContextCompat.getColor(requireContext(), R.color.accent_blue)
            lineWidth = 2.5f
            circleRadius = 4f
            setCircleColor(ContextCompat.getColor(requireContext(), R.color.accent_blue))
            valueTextColor = ContextCompat.getColor(requireContext(), android.R.color.white)
            valueTextSize = 10f
            mode = LineDataSet.Mode.CUBIC_BEZIER // smooth curve
            setDrawValues(false)
            setDrawFilled(true)
            fillColor = ContextCompat.getColor(requireContext(), R.color.accent_blue)
            fillAlpha = 40
        }
        val data = LineData(dataSet)
        chart.data = data
    }

    // Chart Axis Styling
    fun chartLayoutStyling(chart: LineChart){
        // X-axis styling
        val xAxis = chart.xAxis
        xAxis.textColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Y-axis styling
        val leftAxis = chart.axisLeft
        leftAxis.textColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        leftAxis.setDrawGridLines(true)
        leftAxis.gridColor = ContextCompat.getColor(requireContext(), R.color.grey)

        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshStats()
    }
}
