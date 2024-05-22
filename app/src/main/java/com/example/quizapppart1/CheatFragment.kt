package com.example.quizapppart1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResult
import com.example.quizapppart1.databinding.FragmentCheatBinding
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.core.view.MenuHost
import androidx.fragment.app.activityViewModels

class CheatFragment : Fragment() {
    private var _binding: FragmentCheatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCheatBinding.inflate(inflater, container, false)
        val rootView = binding.root
        binding.imSureButton.setOnClickListener { view ->
            viewModel.setCheatedStatusForCurrentQuestion(true)
            binding.cheatAnswer.text = viewModel.questionsAndAnswers[viewModel.questionNum.value ?:0].questionAnswer.toString()
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return rootView
    }
}