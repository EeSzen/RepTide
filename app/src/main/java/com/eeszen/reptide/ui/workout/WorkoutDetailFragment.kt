package com.eeszen.reptide.ui.workout

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.databinding.DialogConfirmationBinding
import com.eeszen.reptide.databinding.FragmentWorkoutDetailBinding
import com.eeszen.reptide.ui.adapter.ExerciseAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WorkoutDetailFragment : Fragment() {
    private lateinit var binding: FragmentWorkoutDetailBinding
    private val args: WorkoutDetailFragmentArgs by navArgs()
    private val viewModel: WorkoutViewModel by viewModels{
        WorkoutViewModel.Factory
    }

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
        lifecycleScope.launch(Dispatchers.IO) {
            val workout = viewModel.getWorkoutById(workoutId)

            if ( workout != null) {
                launch(Dispatchers.Main) {
                    binding.run {
                        tvWorkoutName.text = workout.name
                        tvWorkoutType.text = workout.type.toString()
                        tvExerciseCount.text = "${workout.exercises.size} Exercises"

                        rvExercises.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = ExerciseAdapter(workout.exercises)
                        }

                        // toolbar
                        toolbarTitle.text = getString(R.string.workout_details_title,workout.name)
                        ivBack.setOnClickListener {
                            findNavController().popBackStack()
                        }

                        mbDeleteWorkout.setOnClickListener {
                            showDeleteDialogBox(workout)
                        }

                        mbEditWorkout.setOnClickListener {
                            val action = WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToEditWorkoutFragment(args.workoutId)
                            findNavController().navigate(action)
                        }

                        mbStartWorkout.setOnClickListener {
                            val action = WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToStartWorkoutFragment(args.workoutId)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun showDeleteDialogBox(workout: Workout) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogConfirmationBinding
            .inflate(layoutInflater, null, false)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.root.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnConfirm.setOnClickListener {
            lifecycleScope.launch (Dispatchers.IO) {
                viewModel.deleteWorkout(workout)
                launch (Dispatchers.Main){
                    dialog.dismiss()
                    setFragmentResult("manage_workout",Bundle())
                    findNavController().popBackStack()
                }
            }
        }
        dialog.show()
    }
}