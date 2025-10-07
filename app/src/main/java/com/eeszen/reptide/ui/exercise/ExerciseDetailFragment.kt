package com.eeszen.reptide.ui.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eeszen.reptide.R
import com.eeszen.reptide.databinding.FragmentExerciseDetailBinding

class ExerciseDetailFragment : Fragment() {
    private lateinit var binding: FragmentExerciseDetailBinding
    private val args: ExerciseDetailFragmentArgs by navArgs()
    private val viewModel: ExerciseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExerciseDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exerciseId = args.exerciseId
        val exercise = viewModel.getExerciseById(exerciseId) ?: return

        binding.run {
            tvExerciseName.text = exercise.name
            tvExerciseCategory.text = exercise.category.toString()
            tvExerciseSets.text = getString(R.string.exercise_sets,exercise.sets)
            tvExerciseReps.text = getString(R.string.exercise_reps,exercise.reps)
            tvExerciseDuration.text = getString(R.string.exercise_duration,exercise.duration)

            toolbarTitle.text = getString(R.string.exercise_details_title,exercise.name)
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            mbEditExercise.setOnClickListener {
                val action = ExerciseDetailFragmentDirections.actionExerciseDetailFragmentToEditExerciseFragment(exerciseId)
                findNavController().navigate(action)
            }
        }
    }
}