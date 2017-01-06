package by.mylnikov.transport.viewModel

import android.os.Bundle


open class ViewModel {
    open fun onCreate(savedInstanceState: Bundle?) {
        onRestoreInstanceState(savedInstanceState)
    }

    open fun onSaveInstanceState(outState: Bundle) {
    }

    open fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    }

    open fun onDestroy() {
    }
}
