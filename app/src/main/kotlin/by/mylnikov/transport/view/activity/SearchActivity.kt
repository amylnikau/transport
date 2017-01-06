package by.mylnikov.transport.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import by.mylnikov.transport.R
import by.mylnikov.transport.TransportApplication
import by.mylnikov.transport.databinding.SearchActivityBinding
import by.mylnikov.transport.di.module.SearchActivityModule
import by.mylnikov.transport.viewModel.SearchViewModel

class SearchActivity : BaseActivity<SearchViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<SearchActivityBinding>(this, R.layout.search_activity)
        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val item = menu.findItem(R.id.action_search)
        MenuItemCompat.expandActionView(item)
        MenuItemCompat.setOnActionExpandListener(item, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                finish()
                return true
            }

        })
        val searchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(viewModel.queryTextListener)
        return true
    }

    override fun setupActivityComponent() {
        TransportApplication.appComponent
                .plus(SearchActivityModule(this))
                .inject(this)
    }
}