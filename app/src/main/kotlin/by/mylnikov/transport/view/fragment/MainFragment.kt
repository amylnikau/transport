package by.mylnikov.transport.view.fragment

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.*
import by.mylnikov.transport.R
import by.mylnikov.transport.TransportApplication
import by.mylnikov.transport.api.model.TransportStop
import by.mylnikov.transport.databinding.MainFragmentBinding
import by.mylnikov.transport.di.module.MainFragmentModule
import by.mylnikov.transport.viewModel.MainViewModel


class MainFragment : BaseFragment<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        val viewBinding = DataBindingUtil.inflate<MainFragmentBinding>(inflater, R.layout.main_fragment, container, false)
        viewBinding.viewModel = viewModel
        val drawable = ContextCompat.getDrawable(activity, R.drawable.ic_calendar)
        val wrappedDrawable = DrawableCompat.wrap(drawable).mutate()
        DrawableCompat.setTint(wrappedDrawable, Color.WHITE)
        viewBinding.dateButton.setCompoundDrawablesWithIntrinsicBounds(wrappedDrawable, null, null, null)
        return viewBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val stop: TransportStop = data.getParcelableExtra(MainViewModel.TRANSPORT_STOP)
            if (requestCode == MainViewModel.REQUEST_CODE_DEPARTURE) {
                viewModel.departure = stop
            } else if (requestCode == MainViewModel.REQUEST_CODE_DESTINATION) {
                viewModel.destination = stop
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main, menu)
    }

    override fun setupFragmentComponent() {
        TransportApplication.appComponent
                .plus(MainFragmentModule(this))
                .inject(this)
    }
}