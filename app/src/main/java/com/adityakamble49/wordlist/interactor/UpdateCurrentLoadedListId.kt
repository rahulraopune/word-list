package com.adityakamble49.wordlist.interactor

import com.adityakamble49.wordlist.cache.PreferenceHelper
import javax.inject.Inject

/**
 * Update Current Loaded Word List Use Case
 *
 * @author Aditya Kamble
 * @since 15/4/2018
 */
class UpdateCurrentLoadedListId @Inject constructor(
        private val preferenceHelper: PreferenceHelper) {

    private fun buildUseCaseExecutable(id: Int) {
        preferenceHelper.currentLoadedListId = id
    }

    fun execute(currentLoadedWordListId: Int) = buildUseCaseExecutable(currentLoadedWordListId)
}