package by.mylnikov.transport.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import by.mylnikov.transport.viewModel.ViewModel
import javax.inject.Inject


abstract class BaseActivity<T : ViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivityComponent()
        viewModel.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    protected abstract fun setupActivityComponent()

}
