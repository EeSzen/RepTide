package com.eeszen.reptide.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eeszen.reptide.R
import com.eeszen.reptide.databinding.FragmentHomeBinding
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
            viewModel.totalWorkouts.collectLatest { updateStats() }
        }
        lifecycleScope.launch {
            viewModel.totalExercises.collectLatest { updateStats() }
        }
        lifecycleScope.launch {
            viewModel.lastWorkout.collectLatest { updateStats() }
        }

        // Button -> Go to workout list
        binding.navigateWorkoutButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToWorkoutFragment()
            findNavController().navigate(action)
        }

        binding.navigateExerciseButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToExerciseFragment()
            findNavController().navigate(action)
        }
    }

    fun updateStats(){
        binding.run {
            // Show stats
            statsText.text = getString(
                R.string.stats_text,
                viewModel.totalWorkouts.value,
                viewModel.totalExercises.value
            )

            lastWorkoutText.text = viewModel.lastWorkout.value?.let {
                "Last Workout: ${it.name}"
            } ?: "No workouts yet"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshStats()
    }

}
