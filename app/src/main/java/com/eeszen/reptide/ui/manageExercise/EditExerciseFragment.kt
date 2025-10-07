package com.eeszen.reptide.ui.manageExercise

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.WorkoutType
import kotlinx.coroutines.launch

class EditExerciseFragment : BaseManageExerciseFragment() {
    private val viewModel: EditExerciseViewModel by viewModels()
    private val args: EditExerciseFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupExerciseCategory()

        viewModel.loadExercise(args.exerciseId)

        lifecycleScope.launch {
            viewModel.exercise.collect { exercise ->
                exercise?.let {
                    binding.run {
                        toolbarTitle.text = getString(R.string.update_exercise,it.name)
                        ivBack.setOnClickListener {
                            findNavController().popBackStack()
                        }

                        etName.setText(it.name)

                        // NumberPickers
                        npSets.minValue = 1
                        npSets.maxValue = 10
                        npSets.wrapSelectorWheel = true
                        npSets.value = it.sets

                        npReps.minValue = 1
                        npReps.maxValue = 30
                        npReps.wrapSelectorWheel = true
                        npReps.value = it.reps

                        // Category Chip
                        for (i in 0 until chipGroup.childCount) {
                            val chip = binding.chipGroup.getChildAt(i) as com.google.android.material.chip.Chip
                            chip.isChecked = (chip.tag == it.category)
                        }
                    }
                }
            }
        }

        binding.mbSubmit.setOnClickListener {
            binding.run {
                val name = etName.text.toString()
                val sets = npSets.value
                val reps = npReps.value

                // find the checked chip
                val checkedChipId = chipGroup.checkedChipId
                val checkedChip = chipGroup.findViewById<com.google.android.material.chip.Chip>(checkedChipId)

                val category = checkedChip?.tag as? WorkoutType

                if (category != null) {
                    viewModel.editExercise(name, sets, reps, category)
                } else {
                    showError("Please select a category")
                }
            }
        }

        // observe finish to go back when saved
        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack()
            }
        }

        // observe error
        lifecycleScope.launch {
            viewModel.error.collect{ errorMessage ->
                showError(errorMessage)
            }
        }
    }
}