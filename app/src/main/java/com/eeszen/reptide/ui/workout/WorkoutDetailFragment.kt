package com.eeszen.reptide.ui.workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.databinding.FragmentWorkoutDetailBinding
import com.eeszen.reptide.ui.adapter.ExerciseAdapter


class WorkoutDetailFragment : Fragment() {
    private lateinit var binding: FragmentWorkoutDetailBinding
    private val args: WorkoutDetailFragmentArgs by navArgs()
    private val viewModel: WorkoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWorkoutDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutId = args.workoutId
        val workout = viewModel.getWorkoutById(workoutId) ?: return

        binding.run {
            tvWorkoutName.text = workout.name
            tvWorkoutType.text = workout.type.toString()
            tvExerciseCount.text = "${workout.exercises.size} Exercises"
            tvWorkoutDuration.text = "Duration: ${workout.duration} mins"

            rvExercises.layoutManager = LinearLayoutManager(requireContext())
            rvExercises.adapter = ExerciseAdapter(workout.exercises)
        }
    }
}