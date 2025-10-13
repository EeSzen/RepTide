package com.eeszen.reptide.ui.manageExercise

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.WorkoutType
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class EditExerciseFragment : BaseManageExerciseFragment() {
    private val viewModel: EditExerciseViewModel by viewModels {
        EditExerciseViewModel.Factory
    }

    private val args: EditExerciseFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupExerciseCategory()
        setupToolbar()
        observeExerciseData()
        setupSubmitButton()
        launchCoroutines()
    }

    fun setupToolbar() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun observeExerciseData() {
        viewModel.loadExercise(args.exerciseId)

        lifecycleScope.launch {
            viewModel.exercise.collect { exercise ->
                exercise?.let { updateUI(it.name, it.sets, it.reps, it.category) }
            }
        }
    }

    fun updateUI(name: String, sets: Int, reps: Int, category: WorkoutType) {
        binding.run {
            toolbarTitle.text = getString(R.string.update_exercise, name)
            etName.setText(name)

            npSets.minValue = 1
            npSets.maxValue = 10
            npSets.wrapSelectorWheel = true
            npSets.value = sets

            npReps.minValue = 1
            npReps.maxValue = 30
            npReps.wrapSelectorWheel = true
            npReps.value = reps

            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                chip.isChecked = (chip.tag == category)
            }
        }
    }

    fun setupSubmitButton() {
        binding.mbSubmit.setOnClickListener {
            binding.run {
                val name = etName.text.toString()
                val sets = npSets.value
                val reps = npReps.value

                val checkedChipId = chipGroup.checkedChipId
                val checkedChip =
                    chipGroup.findViewById<Chip>(checkedChipId)
                val category = checkedChip?.tag as? WorkoutType

                if (category != null) {
                    viewModel.editExercise(name, sets, reps, category)
                } else {
                    showError("Please select a category")
                }
            }
        }
    }

    fun launchCoroutines() {
        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack()
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                showError(errorMessage)
            }
        }
    }
}
