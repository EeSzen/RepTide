package com.eeszen.reptide.ui.exercise

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.databinding.FragmentExerciseBinding
import com.eeszen.reptide.ui.adapter.ExerciseAdapter


class ExerciseFragment : Fragment() {
    private val viewModel: ExerciseViewModel by viewModels()

    private lateinit var binding: FragmentExerciseBinding
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
    }

    private fun setupAdapter() {
        exerciseAdapter = ExerciseAdapter(emptyList()) { exercise ->
            val action = ExerciseFragmentDirections
                .actionExerciseFragmentToExerciseDetailFragment(exercise.id ?: 0)
            findNavController().navigate(action)
        }

        binding.run {
            // Recycler View
            rvExercises.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = exerciseAdapter
            }

            // Submit button
            mbSubmit.setOnClickListener {
                val action = ExerciseFragmentDirections.actionExerciseFragmentToAddExerciseFragment()
                findNavController().navigate(action)
            }

            // toolbar
            toolbarTitle.text = getString(R.string.workout_fragment)
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}