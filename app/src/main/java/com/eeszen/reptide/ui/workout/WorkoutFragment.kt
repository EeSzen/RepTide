package com.eeszen.reptide.ui.workout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.databinding.FragmentWorkoutBinding
import com.eeszen.reptide.ui.adapter.WorkoutAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class WorkoutFragment : Fragment() {
    private val viewModel: WorkoutViewModel by viewModels()

    private lateinit var binding: FragmentWorkoutBinding
    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        observeWorkouts()
    }

    private fun setupAdapter() {
        workoutAdapter = WorkoutAdapter(emptyList()) { workout ->
            val action = WorkoutFragmentDirections
                .actionWorkoutFragmentToWorkoutDetailFragment(workout.id ?: 0)
            findNavController().navigate(action)
        }

        binding.run {
            // Recycler View
            rvWorkouts.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = workoutAdapter
            }

            // Submit button
            mbSubmit.setOnClickListener {
                val action = WorkoutFragmentDirections.actionWorkoutFragmentToAddWorkoutFragment()
                findNavController().navigate(action)
            }

            // toolbar
            toolbarTitle.text = getString(R.string.workout_fragment)
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getWorkouts()  // fetch from repo again
    }


    private fun observeWorkouts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.workouts.collect { workouts ->
                workoutAdapter.setWorkouts(workouts)
            }
        }
    }
}
