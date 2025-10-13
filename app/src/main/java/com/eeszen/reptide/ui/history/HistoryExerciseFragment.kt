package com.eeszen.reptide.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.databinding.FragmentHistoryDetailExerciseBinding
import com.eeszen.reptide.ui.adapter.SetHistoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryExerciseFragment : Fragment() {

    private lateinit var binding: FragmentHistoryDetailExerciseBinding
    private val args: HistoryExerciseFragmentArgs by navArgs()
    private val viewModel: HistoryViewModel by viewModels{
        HistoryViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryDetailExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exerciseId = args.exerciseHistoryId
        viewModel.getExerciseHistoryById(exerciseId)

        lifecycleScope.launch {
            viewModel.exerciseHistory.collect { exercise ->
                exercise?.let {
                    binding.run {
                        toolbarTitle.text = it.name
                        tvExerciseName.text = it.name
                        tvExerciseDuration.text = "Duration: ${it.duration ?: 0} min"

                        ivBack.setOnClickListener {
                            findNavController().popBackStack()
                        }

                        rvExerciseSets.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = SetHistoryAdapter(it.sets)
                        }
                    }
                }
            }
        }

        Log.d("HistoryExerciseFragment", "ExerciseLog args: ${args.exerciseHistoryId}")

//        val durationMillis = (exercise.finishedAt ?: 0L) - exercise.startedAt
//        val durationMinutes = durationMillis / 60000


    }
}
