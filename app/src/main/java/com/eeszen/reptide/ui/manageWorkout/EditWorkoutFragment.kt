package com.eeszen.reptide.ui.manageWorkout

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.repo.ExerciseRepo
import com.eeszen.reptide.ui.adapter.ExerciseAdapter
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditWorkoutFragment : BaseManageWorkoutFragment() {
    private val viewModel: EditWorkoutViewModel by viewModels()
    private val args: EditWorkoutFragmentArgs by navArgs()
    private val repo: ExerciseRepo = ExerciseRepo.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWorkoutTypes()

        viewModel.loadWorkout(args.workoutId)

        lifecycleScope.launch {
            viewModel.workout.collect{ workout ->
                workout?.let {
                    binding.run {
                        toolbarTitle.text = getString(R.string.update_workout,it.name)
                        ivBack.setOnClickListener {
                            findNavController().popBackStack()
                        }

                        etName.setText(it.name)

                        // Type Chip
                        for (i in 0 until chipGroup.childCount) {
                            val chip = binding.chipGroup.getChildAt(i) as com.google.android.material.chip.Chip
                            chip.isChecked = (chip.tag == it.type)
                        }

                        // exercises list
                        val adapter = ExerciseAdapter(repo.getAllExercises())
                        adapter.setPreselectedExercises(workout.exercises.map { it.id!! })

                        rvAllExercises.adapter = adapter
                        rvAllExercises.layoutManager = LinearLayoutManager(requireContext())

                        mbSubmit.setOnClickListener {
                            val workoutName = etName.text.toString().trim()
                            // chip logic
                            val checkedId = chipGroup.checkedChipId
                            val selectedChip = chipGroup.findViewById<Chip>(checkedId)
                            val selectedType = selectedChip?.tag as? WorkoutType ?: WorkoutType.PUSH

                            // selected exercises
                            val selectedExercises = adapter.getSelectedExercises()

                            viewModel.editWorkout(
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
        }
    }
}