package com.example.quizapppart1

import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController

class QuizViewModel:ViewModel() {
    val questionsAndAnswers = listOf(
        Question(R.string.question_1, true, false),
        Question(R.string.question_2, false, false),
        Question(R.string.question_3, false, false),
        Question(R.string.question_3, false, false),
        Question(R.string.question_4, false, false),
        Question(R.string.question_5, true, false),
        Question(R.string.question_6, false, false),
        Question(R.string.question_7, true, false),
        Question(R.string.question_8, true, false),
    )
    private var _questionNum = MutableLiveData(0)
    val questionNum: LiveData<Int>
        get() = _questionNum
    private var _numQuestionsCorrect = 0
    val numQuestionsCorrect: Int
        get() = _numQuestionsCorrect
    private var _numWrongAttempts = 0
    val numWrongAttempts: Int
        get() = _numWrongAttempts

    //needs to be live/mutable data
    private var _gameWon = MutableLiveData(false)
    val gameWon: LiveData<Boolean>
        get() = _gameWon

    val currentQuestionAnswer: Boolean
        get() = questionsAndAnswers[_questionNum.value?:0].questionAnswer
    val currentQuestionText: Int
        get() = questionsAndAnswers[_questionNum.value?:0].questionResourceID
    val currentQuestionCheatStatus: Boolean
        get() = questionsAndAnswers[_questionNum.value?:0].userCheated


    fun setCheatedStatusForCurrentQuestion(cheated:Boolean){
        questionsAndAnswers[_questionNum.value?:0].userCheated = cheated
    }

    fun checkIfGameWon(){
        if(_numQuestionsCorrect>=3){
            _gameWon.value=true
        }
    }

    fun previousQuestion() {
        if ((questionNum.value ?: 0) == 0) {
            _questionNum.value = questionsAndAnswers.size-1
        } else {
            _questionNum.value = (_questionNum.value ?:0 )- 1
        }
    }

    //from MainFragment

    fun advanceQuestion() {
        if (_questionNum.value==(questionsAndAnswers.size-1)) {
            _questionNum.value = 0
        } else {
            _questionNum.value = (_questionNum.value ?: 0) + 1
        }
    }

    fun checkAnswer(userAnswer:Boolean):Boolean{
        if (userAnswer == questionsAndAnswers[_questionNum.value ?:0].questionAnswer) {
            if (questionsAndAnswers[_questionNum.value ?:0].userCheated == false) {
                _numQuestionsCorrect = numQuestionsCorrect + 1
                checkIfGameWon()
                return true
            }
            else {
                return true
            }
        } else {
            _numWrongAttempts = numWrongAttempts + 1
            return false
        }
    }

    fun resetVariables(){
        _gameWon.value=false
        _questionNum.value=0
        _numQuestionsCorrect=0
        _numWrongAttempts=0
        for(currentQuestion in questionsAndAnswers){
            currentQuestion.userCheated=false
        }
    }
}