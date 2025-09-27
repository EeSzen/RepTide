package com.eeszen.reptide.ui.manageExercise

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepo
import com.eeszen.reptide.ui.adapter.ExerciseAdapter
import com.google.android.material.chip.Chip

class AddExerciseFragment : BaseManageExerciseFragment() {
    private val viewModel: AddExerciseViewModel by viewModels()
    private val repo : ExerciseRepo = ExerciseRepo.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupExerciseCategory()

        binding.run {
            // toolbar
            toolbarTitle.text = getString(R.string.manage_exercise,"Add New")
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

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

                // Optional: you can set duration = null for now
                viewModel.addExercise(
                    name = exerciseName,
                    category = selectedType,
                    sets = sets,
                    reps = reps
                )
            }
        }
    }

}