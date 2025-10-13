package com.eeszen.reptide.ui.manageWorkout

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.RepTideApp
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepository
import com.eeszen.reptide.ui.adapter.ExerciseAdapter
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class EditWorkoutFragment : BaseManageWorkoutFragment() {
    private val viewModel: EditWorkoutViewModel by viewModels {
        EditWorkoutViewModel.Factory
    }

    private val args: EditWorkoutFragmentArgs by navArgs()
    private lateinit var repo: ExerciseRepository
    private lateinit var adapter: ExerciseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = (requireActivity().application as RepTideApp).exerciseRepository

        setupWorkoutTypes()
        setupObservers()
    }

    fun setupObservers() {
        viewModel.loadWorkout(args.workoutId)

        lifecycleScope.launch {
            viewModel.workout.collect { workout ->
                workout?.let {
                    setupToolbar(it.name)
                    setupWorkoutFields(it.name, it.type)
                    setupExerciseList(it)
                    setupSubmitButton(it)
                    launchCoroutines()
                }
            }
        }
    }

    fun setupToolbar(name: String) {
        binding.toolbarTitle.text = getString(R.string.update_workout, name)
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun setupWorkoutFields(name: String, type: WorkoutType) {
        binding.etName.setText(name)
        for (i in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(i) as Chip
            chip.isChecked = (chip.tag == type)
        }
    }

    fun setupExerciseList(workout: Workout) {
        adapter = ExerciseAdapter(emptyList(), onCheckedChange = { _, _ -> })
        binding.rvAllExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllExercises.adapter = adapter

        lifecycleScope.launch {
            repo.getAllExercises().collect { exercises ->
                val preselectedIds = workout.exercises.mapNotNull { it.id }
                val updatedList = exercises.map {
                    it.copy(isSelected = preselectedIds.contains(it.id))
                }
                adapter.setExercises(updatedList)
            }
        }
    }

    fun setupSubmitButton(workout: Workout) {
        binding.mbSubmit.setOnClickListener {
            val name = binding.etName.text.toString().trim()

            val checkedId = binding.chipGroup.checkedChipId
            val selectedChip = binding.chipGroup.findViewById<Chip>(checkedId)
            val type = selectedChip?.tag as? WorkoutType ?: WorkoutType.PUSH

            val selectedExercises = adapter.getSelectedExercises()

            viewModel.editWorkout(
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
