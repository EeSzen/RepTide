package com.eeszen.reptide.ui.manageExercise

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepo
import com.eeszen.reptide.ui.adapter.ExerciseAdapter
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class AddExerciseFragment : BaseManageExerciseFragment() {
    private val viewModel: AddExerciseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupExerciseCategory()

        binding.run {
            // toolbar
            toolbarTitle.text = getString(R.string.manage_exercise,"Add New")
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            // Setup NumberPickers
            npSets.minValue = 1
            npSets.maxValue = 10
            npSets.wrapSelectorWheel = true

            npReps.minValue = 1
            npReps.maxValue = 30
            npReps.wrapSelectorWheel = true

            // Submit Button
            mbSubmit.setOnClickListener {
                val exerciseName = etName.text.toString().trim()

                // Category
                val checkedId = chipGroup.checkedChipId
                val selectedChip = chipGroup.findViewById<Chip>(checkedId)
                val selectedType = selectedChip?.tag as? WorkoutType ?: WorkoutType.PUSH

                // NumberPickers
                val sets = npSets.value
                val reps = npReps.value

                viewModel.addExercise(
                    name = exerciseName,
                    category = selectedType,
                    sets = sets,
                    reps = reps
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