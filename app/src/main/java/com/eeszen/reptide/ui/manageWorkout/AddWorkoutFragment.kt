package com.eeszen.reptide.ui.manageWorkout

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepo
import com.eeszen.reptide.ui.adapter.ExerciseAdapter
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class AddWorkoutFragment : BaseManageWorkoutFragment() {
    private val viewModel: AddWorkoutViewModel by viewModels()
    private val repo : ExerciseRepo = ExerciseRepo.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWorkoutTypes()

        binding.run {
            // toolbar
            toolbarTitle.text = getString(R.string.manage_workout,"Add New")
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            // exercises list
            val adapter = ExerciseAdapter(repo.getAllExercises())
            rvAllExercises.adapter = adapter
            rvAllExercises.layoutManager = LinearLayoutManager(requireContext())

            // Submit Button
            mbSubmit.setOnClickListener {
                val workoutName = etName.text.toString().trim()
                // chip logic
                val checkedId = chipGroup.checkedChipId
                val selectedChip = chipGroup.findViewById<Chip>(checkedId)
                val selectedType = selectedChip?.tag as? WorkoutType?:WorkoutType.PUSH

                // selected exercises
                val selectedExercises = adapter.getSelectedExercises()

                viewModel.addWorkout(
                    name = workoutName,
                    type = selectedType,
                    exercises = selectedExercises
                )
            }
            //  collect flows
            lifecycleScope.launch {
                viewModel.error.collect{ errorMessage ->
                    showError(errorMessage)
                }
            }

            lifecycleScope.launch {
                viewModel.finish.collect{
                    findNavController().popBackStack()
                }
            }


        }
    }
}