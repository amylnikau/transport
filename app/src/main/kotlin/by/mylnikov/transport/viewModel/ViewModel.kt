package by.mylnikov.transport.viewModel

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class ViewModel {

    private var mCompositeDisposable = CompositeDisposable()
    open fun onCreate(savedInstanceState: Bundle?) {
        mCompositeDisposable = CompositeDisposable()
        onRestoreInstanceState(savedInstanceState)
    }

    open fun onSaveInstanceState(outState: Bundle) {
    }

    open fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    }

    open fun onDestroy() {
        mCompositeDisposable.dispose()
    }

    fun addToDisposables(disposable: Disposable){
        mCompositeDisposable.add(disposable)
    }
}
