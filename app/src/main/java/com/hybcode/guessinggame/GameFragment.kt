package com.hybcode.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.Observer
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

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        _binding = FragmentGameBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                MaterialTheme {
                    Surface {
                        GameFragmentContent(viewModel)
                    }
                }
            }
        }
        val view = binding.root



        binding.gameViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

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


    @Composable
    fun GameFragmentContent(viewModel: GameViewModel) {
        val guess = remember{ mutableStateOf("")}
        Column(
            modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
                SecretWordDisplay(viewModel)
            }
            LivesLeftText(viewModel)
            IncorrectGuessesText(viewModel)
            EnterGuess(guess = guess.value){
                guess.value = it
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                GuessButton{
                    viewModel.makeGuess(guess.value.uppercase())
                    guess.value = ""
                }
                FinishGameButton {
                    viewModel.finishGame()
                }
            }
        }
    }
    
    @Composable
    fun SecretWordDisplay(viewModel: GameViewModel){
        val secretWordDisplay = viewModel.secretWordDisplay.observeAsState()
        secretWordDisplay.value?.let {
            Text(it)
        }
    }

    @Composable
    fun LivesLeftText(viewModel: GameViewModel){
        val livesLeft = viewModel.livesLeft.observeAsState()
        livesLeft.value?.let {
            Text(stringResource(id = R.string.lives_left, it))
        }
    }

    @Composable
    fun IncorrectGuessesText(viewModel: GameViewModel){
        val incorrectGuesses = viewModel.incorrectGuesses.observeAsState()
        incorrectGuesses.value?.let {
            Text(stringResource(id = R.string.incorrect_guesses, it))
        }
    }
    @Composable
    fun GuessButton(clicked: () -> Unit){
        Button(onClick = clicked) {
            Text(text = "Guess")
        }
    }

    @Composable
    fun EnterGuess(guess: String, changed:(String)->Unit){
        TextField(value = guess,
            label = { Text(text = "Guess a letter")},
            onValueChange = changed)
    }

    @Composable
    fun FinishGameButton(clicked: () -> Unit) {
        Button(clicked) {
            Text(text = "Finish Game")
        }
    }

}