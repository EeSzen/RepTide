package com.eeszen.reptide.ui.manageWorkout

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepository
import com.eeszen.reptide.ui.adapter.ExerciseAdapter
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class AddWorkoutFragment : BaseManageWorkoutFragment() {
    private val viewModel: AddWorkoutViewModel by viewModels {
        AddWorkoutViewModel.Factory
    }

    private lateinit var repo: ExerciseRepository
    private lateinit var adapter: ExerciseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repo = (requireActivity().application as RepTideApp).exerciseRepository
        setupWorkoutTypes()
        setupToolbar()
        setupExerciseList()
        setupSubmitButton()
        launchCoroutines()
    }

    fun setupToolbar() {
        binding.run {
            toolbarTitle.text = getString(R.string.manage_workout, "Add New")
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    fun setupExerciseList() {
        adapter = ExerciseAdapter(emptyList(), onCheckedChange = { _, _ -> })
        binding.rvAllExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllExercises.adapter = adapter

        lifecycleScope.launch {
            repo.getAllExercises().collect { exercises ->
                adapter.setExercises(exercises)
            }
        }
    }

    fun setupSubmitButton() {
        binding.mbSubmit.setOnClickListener {
            val name = binding.etName.text.toString().trim()

            val checkedId = binding.chipGroup.checkedChipId
            val selectedChip = binding.chipGroup.findViewById<Chip>(checkedId)
            val type = selectedChip?.tag as? WorkoutType ?: WorkoutType.PUSH

            val selectedExercises = adapter.getSelectedExercises()

            viewModel.addWorkout(
                name = name,
                type = type,
                exercises = selectedExercises
            )
        }
    }

    fun launchCoroutines() {
        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                showError(errorMessage)
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack()
            }
        }
    }
}
