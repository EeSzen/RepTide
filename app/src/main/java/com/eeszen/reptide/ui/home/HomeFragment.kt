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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

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

        lifecycleScope.launch {
            viewModel.totalWeight.collectLatest { total ->
                binding.tvHomeVolumeLifted.text = getString(R.string.total_volume_lifted, total)
            }
        }

        lifecycleScope.launch {
            viewModel.highestWeight.collectLatest { pr ->
                binding.tvHomePR.text = pr.toString()
            }
        }

        lifecycleScope.launch {
            viewModel.lastWorkoutType.collectLatest { type ->
                binding.tvHomeWorkoutType.text = type?.name ?: "NULL"
            }
        }

        binding.navigateWorkoutFragment.setOnClickListener {
            (requireActivity() as MainActivity).selectBottomNavItem(R.id.workoutFragment)
        }


        setupWeightChart()
    }

    private fun setupWeightChart() {
        val chart = binding.weightLineChart

        // Basic chart settings
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.setDrawGridBackground(false)
        chart.setNoDataText("No progress data yet")
        chart.setNoDataTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
        chart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_gray))

        // Progress Data
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

        // Legend
        val legend = chart.legend
        legend.isEnabled = false

        chart.animateX(1000)
        chart.invalidate()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshStats()
    }
}
