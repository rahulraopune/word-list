package com.adityakamble49.wordlist.ui.word

import android.arch.lifecycle.LifecycleOwner
import com.adityakamble49.wordlist.model.Word
import com.adityakamble49.wordlist.ui.common.BaseContract
import com.adityakamble49.wordlist.ui.common.OnSwipeTouchListener

/**
 * Word View Pager Contract
 *
 * @author Aditya Kamble
 * @since 8/4/2018
 */
interface WordContract {

    interface View : BaseContract.View, LifecycleOwner {
        fun initializeActivityMode(currentWordActivityMode: Int)
        fun updateWord(word: Word)
        fun updateWordInformation(information: String)
        fun updateWordMnemonic(mnemonic: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun setWordViewModel(wordViewModel: WordViewModel)
        fun setActivityMode(wordActivityMode: Int)
        fun setWordId(wordId: Int)
        fun onPause()
        fun onClickWordInformation()
        fun onClickWordMnemonic()
        fun onSwipe(swipeDirection: OnSwipeTouchListener.SwipeDirection)
    }
}