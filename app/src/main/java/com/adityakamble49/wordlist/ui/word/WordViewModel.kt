package com.adityakamble49.wordlist.ui.word

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.adityakamble49.wordlist.model.DictateModeConfig
import com.adityakamble49.wordlist.model.Word
import com.adityakamble49.wordlist.model.WordList
import javax.inject.Inject

/**
 * Word View Model
 *
 * @author Aditya Kamble
 * @since 8/4/2018
 */
class WordViewModel @Inject constructor() : ViewModel() {

    var currentWordPosition: Int = 0
    lateinit var currentWord: Word
    lateinit var currentWordList: WordList
    lateinit var wordList: List<Word>
    var wordListPractice: List<Word> = mutableListOf()
    lateinit var singleWord: LiveData<Word>
    var isDictateModeOn = false
    lateinit var dictateModeConfig: DictateModeConfig

    fun setupSingleWord(word: LiveData<Word>) {
        singleWord = word
    }
}