package com.adityakamble49.wordlist.interactor

import com.adityakamble49.wordlist.cache.PreferenceHelper
import com.adityakamble49.wordlist.cache.db.WordListRepo
import com.adityakamble49.wordlist.cache.db.WordRepo
import com.adityakamble49.wordlist.model.Word
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Build Get Word List UseCase
 *
 * @author Aditya Kamble
 * @since 8/4/2018
 */
class GetWordListUseCase @Inject constructor(
        private val preferenceHelper: PreferenceHelper,
        private val wordRepo: WordRepo,
        private val wordListRepo: WordListRepo) {

    private fun buildUseCaseObservable(): Observable<List<Word>> {
        return Observable.create { e ->
            val currentLoadedListId = preferenceHelper.currentLoadedListId
            val currentWordList = wordListRepo.getWordListById(currentLoadedListId)
            val wordList = wordRepo.getWordListDirect(currentWordList.listType)
            e.onNext(wordList)
            e.onComplete()
        }
    }

    fun execute(): Observable<List<Word>> {
        return buildUseCaseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}