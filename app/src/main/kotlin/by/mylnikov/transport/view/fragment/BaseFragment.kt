package by.mylnikov.transport.view.fragment

import android.app.Fragment
import android.os.Bundle
import by.mylnikov.transport.viewModel.ViewModel
import javax.inject.Inject


abstract class BaseFragment<T : ViewModel> : Fragment() {

    @Inject
    lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentComponent()
        viewModel.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    abstract fun setupFragmentComponent()
}