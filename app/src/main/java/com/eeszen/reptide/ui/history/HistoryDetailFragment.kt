package com.eeszen.reptide.ui.history

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.Exercise
import com.eeszen.reptide.data.model.logs.WorkoutLog
import com.eeszen.reptide.databinding.DialogConfirmationBinding
import com.eeszen.reptide.databinding.FragmentHistoryDetailBinding
import com.eeszen.reptide.ui.adapter.ExerciseHistoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryDetailFragment : Fragment() {

    private lateinit var binding: FragmentHistoryDetailBinding
    private val args: HistoryDetailFragmentArgs by navArgs()
    private val viewModel : HistoryViewModel by viewModels{
        HistoryViewModel.Factory
    }

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

        lifecycleScope.launch (Dispatchers.IO){
            val history = viewModel.getHistoryById(historyId)

            if (history != null){
                launch (Dispatchers.Main){

                    // setup exercise grid
                    val exerciseAdapter = ExerciseHistoryAdapter(history.exercises) { exercise ->
                        Log.d("HistoryDetailFragment", "Exercise clicked: ${exercise.name}, id=${exercise.exerciseId}")
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

                        mbDeleteHistory.setOnClickListener {
                            showDeleteDialogBox(history)
                        }
                    }

                    binding.rvHistoryExercises.apply {
                        layoutManager = GridLayoutManager(requireContext(), 2)
                        adapter = exerciseAdapter
                    }
                }
            }
        }
    }

    private fun showDeleteDialogBox(history:WorkoutLog) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogConfirmationBinding
            .inflate(layoutInflater, null, false)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.root.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnConfirm.setOnClickListener {
            lifecycleScope.launch (Dispatchers.IO) {
                viewModel.deleteHistory(history)
                launch (Dispatchers.Main){
                    dialog.dismiss()
                    setFragmentResult("history_updated",Bundle())
                    findNavController().popBackStack()
                }
            }
        }
        dialog.show()
    }


    // date time convertor
    private fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return "Unknown date"
        val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
