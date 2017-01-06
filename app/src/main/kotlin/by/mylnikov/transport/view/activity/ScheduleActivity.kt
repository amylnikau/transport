package by.mylnikov.transport.view.activity

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import by.mylnikov.transport.R
import by.mylnikov.transport.TransportApplication
import by.mylnikov.transport.databinding.ScheduleActivityBinding
import by.mylnikov.transport.di.module.ScheduleActivityModule
import by.mylnikov.transport.viewModel.ScheduleViewModel

class ScheduleActivity : BaseActivity<ScheduleViewModel>() {

    companion object {
        const val DEPARTURE_NAME = "fromName"
        const val DESTINATION_NAME = "toName"
        const val DEPARTURE_KEY = "fromId"
        const val DESTINATION_KEY = "toId"
        const val DEPARTURE_DATE = "when"
        const val EXTRA = "extra_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = DataBindingUtil.setContentView<ScheduleActivityBinding>(this, R.layout.schedule_activity)
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val collapsingLayout = binding.toolbarLayout
        collapsingLayout.setExpandedTitleColor(Color.TRANSPARENT)
        binding.appBar.addOnOffsetChangedListener(viewModel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupActivityComponent() {
        TransportApplication.appComponent
                .plus(ScheduleActivityModule(this))
                .inject(this)
    }
}
