package com.example.quizapppart1

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import com.example.quizapppart1.databinding.ActivityMainBinding
import com.example.quizapppart1.databinding.FragmentMainBinding
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder


//constants to save values when rotating



class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    lateinit var mediaPlayer: MediaPlayer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val rootView = binding.root



        binding.trueButton.setOnClickListener { view ->
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener { view ->
            checkAnswer(false)
        }


        binding.question.setOnClickListener { view ->
            viewModel.advanceQuestion()
        }
        binding.forwardArrow.setOnClickListener { view ->
            viewModel.advanceQuestion()
        }
        binding.backwardArrow.setOnClickListener { view ->
            viewModel.previousQuestion()
        }




        binding.cheatButton.setOnClickListener { view ->
            val passIn =
                MainFragmentDirections.actionMainFragmentToCheatFragment()
            rootView.findNavController().navigate(passIn)
        }


        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(menuItem, requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        mediaPlayer = MediaPlayer.create(context,R.raw.correctsound)


        //observers

        viewModel.questionNum.observe(viewLifecycleOwner) { newQuestionNum ->
            binding.question.text = getString(viewModel.currentQuestionText)

        }
        viewModel.gameWon.observe(viewLifecycleOwner) { gameWonStatus ->
           if(gameWonStatus) {
               MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                   .setTitle(getString(R.string.game_won_alert_title))
                   .setMessage(getString(R.string.game_won_alert_body))
                   .setNegativeButton("No"){dialog, which ->
                       val passIn =
                           MainFragmentDirections.actionMainFragmentToGameWonFragment()
                       binding.root.findNavController().navigate(passIn)
                   }
                   .setPositiveButton("Yes"){dialog, which ->
                       viewModel.resetVariables()
                   }

                   .show()

           }
        }




        return rootView
    }





    override fun onStop() {
        super.onStop()
        mediaPlayer.release()
    }

    //keep toasts in mainfragment also keep part here
    fun checkAnswer(userAnswer: Boolean) {
        if (viewModel.checkAnswer(userAnswer)) {
            if (viewModel.currentQuestionCheatStatus) {
                Toast.makeText(activity, R.string.cheat_toast_text, Toast.LENGTH_SHORT)
                    .show()
            } else if (viewModel.currentQuestionCheatStatus == false) {
                Toast.makeText(activity, R.string.right_answer, Toast.LENGTH_SHORT).show()
//                if (viewModel.numQuestionsCorrect == 3) {
//                    val passIn =
//                        MainFragmentDirections.actionMainFragmentToGameWonFragment(
//                            viewModel.numWrongAttempts
//                        )
//                    binding.root.findNavController().navigate(passIn)
//                }
                mediaPlayer = MediaPlayer.create(context,R.raw.correctsound)
                mediaPlayer.start()
            }
        } else {
            Toast.makeText(activity, R.string.wrong_answer, Toast.LENGTH_SHORT).show()
            mediaPlayer = MediaPlayer.create(context,R.raw.wrongsound)
            mediaPlayer.start()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}