package com.hybcode.guessinggame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    private val words = listOf("Android", "Activity", "Fragment", "Navigation", "Binding")
    private val secretWord = words.random().uppercase()
    private var correctGuesses = ""


    private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay: LiveData<String> get() = _secretWordDisplay

    private val _incorrectGuesses = MutableLiveData<String>("")
    val incorrectGuesses: LiveData<String> get() = _incorrectGuesses

    private val _livesLeft = MutableLiveData<Int>(8)
    val livesLeft:LiveData<Int> get() = _livesLeft

    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver: LiveData<Boolean> get() = _gameOver

    init {
        _secretWordDisplay.value = deriveSecretWordDisplay()
    }

    private fun deriveSecretWordDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter(letter: String) = when (correctGuesses.contains(letter)) {
        true -> letter
        false -> "_"
    }

    fun makeGuess(guess: String) {
        if (guess.length == 1){
            if (secretWord.contains(guess)){
                correctGuesses += guess
                _secretWordDisplay.value = deriveSecretWordDisplay()
            }else{
                _incorrectGuesses.value += guess
                _livesLeft.value = livesLeft.value?.minus(1)
            }
        }
        if (isWon() || isLose()) _gameOver.value = true
    }

    private fun isWon() = secretWord.equals(secretWordDisplay.value, true)

    private fun isLose() = livesLeft.value ?: 0 <= 0

    fun wonLoseMessage(): String {
        var message = ""
        if(isWon()) message = "You won!"
        else if (isLose()) message = "You lose!"
        message += " The word was $secretWord."
        return message
    }

    fun finishGame(){
        _gameOver.value = true
    }
}