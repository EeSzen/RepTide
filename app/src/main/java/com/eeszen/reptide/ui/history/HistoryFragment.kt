package com.eeszen.reptide.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eeszen.reptide.R
import com.eeszen.reptide.databinding.FragmentHistoryBinding
import com.eeszen.reptide.ui.adapter.HistoryAdapter

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val completedLogs = viewModel.getCompletedWorkouts()

        historyAdapter = HistoryAdapter(completedLogs) { workout ->
            val action = HistoryFragmentDirections
                .actionHistoryFragmentToHistoryDetailFragment(workout.id ?: 0)
            findNavController().navigate(action)
        }

        binding.run {
            rvHistory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = historyAdapter
            }
        }
    }
}
