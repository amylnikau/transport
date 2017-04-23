package by.mylnikov.transport.view.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.*
import by.mylnikov.transport.R
import by.mylnikov.transport.TransportApplication
import by.mylnikov.transport.databinding.FavoritesFragmentBinding
import by.mylnikov.transport.di.module.FavoritesFragmentModule
import by.mylnikov.transport.viewModel.FavoritesViewModel


class FavoritesFragment : BaseFragment<FavoritesViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        val viewBinding = DataBindingUtil.inflate<FavoritesFragmentBinding>(inflater, R.layout.favorites_fragment, container, false)
        viewBinding.viewModel = viewModel
        return viewBinding.root
    }

    override fun setupFragmentComponent() {
        TransportApplication.appComponent
                .plus(FavoritesFragmentModule(this))
                .inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main, menu)
    }

}