package com.adityakamble49.wordlist.interactor

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base Use Case
 *
 * @author Aditya Kamble
 * @since 28/6/2018
 */
abstract class BaseRxUseCase {

    private var disposables = CompositeDisposable()

    protected fun addDisposables(disposable: Disposable) {
        if (disposables.isDisposed) {
            disposables = CompositeDisposable()
        }
        disposables.add(disposable)
    }

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}