package com.hybcode.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hybcode.guessinggame.databinding.FragmentGameBinding
import androidx.navigation.findNavController


class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

//        viewModel.incorrectGuesses.observe(viewLifecycleOwner, Observer { guess ->
//            binding.incorrectGuesses.text = getString(R.string.incorrect_guesses, guess)
//        })
//
//        viewModel.livesLeft.observe(viewLifecycleOwner, Observer { lives ->
//            binding.lives.text = getString(R.string.lives_left, lives)
//        })
//
//        viewModel.secretWordDisplay.observe(viewLifecycleOwner, Observer { secretWord ->
//            binding.word.text = secretWord
//        })

        viewModel.gameOver.observe(viewLifecycleOwner, Observer {
            if (it) {
                val action = GameFragmentDirections
                    .actionGameFragmentToResultFragment(viewModel.wonLoseMessage())
                view.findNavController().navigate(action)
            }
        })

        binding.guessButton.setOnClickListener {
            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text = null
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}