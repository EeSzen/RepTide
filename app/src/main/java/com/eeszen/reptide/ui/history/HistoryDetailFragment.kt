package com.eeszen.reptide.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.databinding.FragmentHistoryDetailBinding
import com.eeszen.reptide.ui.adapter.ExerciseHistoryAdapter
import java.text.SimpleDateFormat
import java.util.*

class HistoryDetailFragment : Fragment() {

    private lateinit var binding: FragmentHistoryDetailBinding
    private val args: HistoryDetailFragmentArgs by navArgs()
    private val viewModel : HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historyId = args.historyId
        val history = viewModel.getHistoryById(historyId) ?: return

        // setup exercise grid
        val exerciseAdapter = ExerciseHistoryAdapter(history.exercises) { exercise ->
            Log.d("HistoryDetailFragment", "Exercise clicked: ${exercise.name}, id=${exercise.id}")

            val action = HistoryDetailFragmentDirections
                .actionHistoryDetailFragmentToHistoryExerciseFragment(exercise.id ?: -1)
            findNavController().navigate(action)
        }

        binding.run {
            tvHistoryName.text = history.name
            tvHistoryType.text = history.type.toString()
            tvHistoryDate.text = formatDate(history.finishedAt)

            val durationMillis = (history.finishedAt ?: 0L) - history.startedAt
            val durationMinutes = durationMillis / 60000
            tvHistoryDuration.text = getString(R.string.workout_duration,durationMinutes)

            toolbarTitle.text = getString(R.string.history_details_title,history.name)
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.rvHistoryExercises.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = exerciseAdapter
        }
    }



    // date time convertor
    private fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return "Unknown date"
        val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
