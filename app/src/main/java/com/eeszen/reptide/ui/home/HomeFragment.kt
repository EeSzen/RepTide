package com.eeszen.reptide.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eeszen.reptide.R
import com.eeszen.reptide.databinding.FragmentHomeBinding

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

        // Show stats
        binding.statsText.text = getString(
            R.string.stats_text,
            viewModel.totalWorkouts,
            viewModel.totalExercises
        )


        // Show last workout
        binding.lastWorkoutText.text = viewModel.lastWorkout?.let {
            "Last Workout: ${it.name}"
        } ?: "No workouts yet"

        // Button -> Go to workout list
        binding.startWorkoutButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToWorkoutFragment()
            findNavController().navigate(action)
        }
    }
}
