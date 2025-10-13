package com.eeszen.reptide.ui.exercise

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
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.databinding.DialogConfirmationBinding
import com.eeszen.reptide.databinding.FragmentExerciseDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseDetailFragment : Fragment() {
    private lateinit var binding: FragmentExerciseDetailBinding
    private val args: ExerciseDetailFragmentArgs by navArgs()
    private val viewModel: ExerciseViewModel by viewModels{
        ExerciseViewModel.Factory
    }

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
        lifecycleScope.launch(Dispatchers.IO) {
            val exercise = viewModel.getExerciseById(exerciseId)

            if (exercise != null) {
                // Switch back to main thread to update UI
                launch(Dispatchers.Main) {
                    binding.run {
                        tvExerciseName.text = exercise.name
                        tvExerciseCategory.text = exercise.category.toString()
                        tvExerciseSets.text = getString(R.string.exercise_sets, exercise.sets)
                        tvExerciseReps.text = getString(R.string.exercise_reps, exercise.reps)

                        toolbarTitle.text =
                            getString(R.string.exercise_details_title, exercise.name)

                        ivBack.setOnClickListener {
                            findNavController().popBackStack()
                        }

                        mbDeleteExercise.setOnClickListener {
                            showDeleteDialogBox(exercise)
                        }

                        mbEditExercise.setOnClickListener {
                            val action = ExerciseDetailFragmentDirections
                                .actionExerciseDetailFragmentToEditExerciseFragment(exerciseId)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun showDeleteDialogBox(exercise: Exercise) {
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
                viewModel.deleteExercise(exercise)
                launch (Dispatchers.Main){
                    dialog.dismiss()
                    setFragmentResult("manage_exercise",Bundle())
                    findNavController().popBackStack()
                }
            }
        }
        dialog.show()
    }
}