package com.eeszen.reptide.ui.startWorkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.Workout
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.data.model.logs.ExerciseLog
import com.eeszen.reptide.data.model.logs.SetLog
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.data.repo.WorkoutRepo
import com.eeszen.reptide.databinding.FragmentStartWorkoutBinding
import com.eeszen.reptide.ui.adapter.StartExerciseAdapter
import com.google.android.material.chip.Chip

class StartWorkoutFragment : Fragment() {
    private val args: StartWorkoutFragmentArgs by navArgs()
    private lateinit var binding: FragmentStartWorkoutBinding
    private val viewModel : StartWorkoutViewModel by viewModels()
    private lateinit var startExerciseAdapter: StartExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartWorkoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutId = args.workoutId
        val workout = viewModel.getWorkoutById(workoutId) ?: return
        val workoutLog = workout.toWorkoutLog()  // convert

        binding.run {
            toolbarTitle.text = workout.name
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

//            // Setup ViewPager with adapter
            startExerciseAdapter = StartExerciseAdapter(workoutLog.exercises)
            viewPagerExercises.adapter = startExerciseAdapter

            setupChipNavigation(workout)
            syncChipsWithPager(workout)
        }
    }

    // Mapper Function (convert Exercise to ExerciseLog)
    fun Exercise.toExerciseLog(): ExerciseLog {
        return ExerciseLog(
            exerciseId = id,
            name = name,
            category = category,
            sets = (1..sets).map { setNum ->
                SetLog(
                    setNumber = setNum,
                    plannedReps = reps,
                    plannedWeight = 0.0 // default for now
                )
            }
        )
    }

    // Mapper Function (convert Workout to WorkoutLog)
    fun Workout.toWorkoutLog(): WorkoutLog {
        return WorkoutLog(
            workoutId = id,
            name = name,
            type = type,
            exercises = exercises.map { it.toExerciseLog() }
        )
    }


    // Setup The Chips
    protected fun setupChipNavigation(workout:Workout) {
        binding.chipGroupExercises.removeAllViews()
        workout.exercises.forEachIndexed { index,exercise ->
            val chip = Chip(requireContext()).apply {
                text = exercise.name
                isCheckable = true
                tag = exercise
            }
            if (index == 0) chip.isChecked = true
            binding.chipGroupExercises.addView(chip)
        }
    }

    // Sync Chips <-> ViewPager
    private fun syncChipsWithPager(workout: Workout) {
        // Chip → ViewPager
        binding.chipGroupExercises.setOnCheckedChangeListener { group, checkedId ->
            val index = group.indexOfChild(group.findViewById(checkedId))
            if (index != -1) binding.viewPagerExercises.currentItem = index
        }

        // ViewPager → Chip
        binding.viewPagerExercises.registerOnPageChangeCallback(
            object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val chip = binding.chipGroupExercises.getChildAt(position) as Chip
                    chip.isChecked = true
                }
            }
        )
    }
}
