package com.eeszen.reptide.ui.manageExercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.eeszen.reptide.R
import com.eeszen.reptide.data.model.WorkoutType
import com.eeszen.reptide.databinding.FragmentBaseManageExerciseBinding
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

open class BaseManageExerciseFragment : Fragment() {
    protected lateinit var binding: FragmentBaseManageExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBaseManageExerciseBinding.inflate(inflater,container,false)
        return binding.root
    }

    protected fun setupExerciseCategory() {
        binding.chipGroup.removeAllViews()
        WorkoutType.entries.forEach { type ->
            val chip = Chip(requireContext()).apply {
                text = type.name
                isCheckable = true
                tag = type
                isChecked = (type == WorkoutType.PUSH)
            }
            binding.chipGroup.addView(chip)
        }
    }

    fun showError(msg:String){
        val snackbar = Snackbar.make(binding.root,msg, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
        snackbar.show()
    }
}