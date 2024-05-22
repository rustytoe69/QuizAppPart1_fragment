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
import androidx.core.view.MenuProvider
import com.example.quizapppart1.databinding.FragmentGameWonBinding
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.core.view.MenuHost
import androidx.fragment.app.activityViewModels

class GameWonFragment : Fragment() {
    private var _binding: FragmentGameWonBinding? = null
    private val binding get() = _binding!!


    private val viewModel: QuizViewModel by activityViewModels()

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameWonBinding.inflate(inflater, container, false)
        val rootView = binding.root
        binding.numWrongAttemptsText.text="You had ${viewModel.numWrongAttempts} wrong answers :)"

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(menuItem, requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        mediaPlayer = MediaPlayer.create(context,R.raw.song)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        var currentPosition: Int=0
        binding.rewindButton.setOnClickListener{view ->
            currentPosition=mediaPlayer.currentPosition
            mediaPlayer.seekTo(currentPosition-10000)
        }
        binding.fastforwardButton.setOnClickListener{view ->
            currentPosition=mediaPlayer.currentPosition
            mediaPlayer.seekTo(currentPosition+10000)

        }
        binding.pauseplayButton.setOnClickListener{view ->
            currentPosition=mediaPlayer.currentPosition
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                binding.pauseplayButton.setImageResource(R.drawable.ic_play)
            }
            else{
                mediaPlayer.start()
                mediaPlayer.seekTo(currentPosition)
                binding.pauseplayButton.setImageResource(R.drawable.ic_pause)
            }
        }


        return rootView
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.release()
    }
}