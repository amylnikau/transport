package by.mylnikov.transport.view.activity

import android.animation.ValueAnimator
import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import by.mylnikov.transport.R
import by.mylnikov.transport.databinding.MainActivityBinding
import by.mylnikov.transport.view.fragment.FavoritesFragment
import by.mylnikov.transport.view.fragment.MainFragment
import by.mylnikov.transport.view.fragment.SettingsFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mDrawer: DrawerLayout
    private var mBackPressed: Long = 0
    private var mIsBackStackEmpty = true

    companion object {
        private const val TIME_INTERVAL = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        val toolbar = binding.toolbar
        val navView = binding.navView
        setSupportActionBar(toolbar)
        mDrawer = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        fragmentManager.addOnBackStackChangedListener {
            val start: Float
            if (fragmentManager.backStackEntryCount > 0) {
                mIsBackStackEmpty = false
                start = 0.toFloat()
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }
            } else {
                start = 1.toFloat()
                toolbar.setNavigationOnClickListener {
                    if (mDrawer.isDrawerVisible(GravityCompat.START)) {
                        mDrawer.closeDrawer(GravityCompat.START)
                    } else {
                        mDrawer.openDrawer(GravityCompat.START)
                    }
                }
            }
            val anim = ValueAnimator.ofFloat(start, 1 - start)
            anim.addUpdateListener {
                valueAnimator ->
                toggle.onDrawerSlide(mDrawer, valueAnimator.animatedValue as Float)
            }
            anim.interpolator = DecelerateInterpolator()
            anim.duration = 300
            anim.start()

        }
        if (savedInstanceState == null) {
            navView.setCheckedItem(R.id.nav_home)
            navView.menu.performIdentifierAction(R.id.nav_home, 0)
        }
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
        } else {
            if(!mIsBackStackEmpty){
                mBackPressed = 0
                mIsBackStackEmpty = true
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                super.onBackPressed()
            }
            else if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed()
            } else {
                mBackPressed = System.currentTimeMillis()
                Toast.makeText(baseContext, getString(R.string.exit_message), Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return selectItem(item.itemId)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return selectItem(item.itemId)
    }

    private fun selectItem(itemId: Int): Boolean {
        val fragment: Fragment
        var isAddToBackStack = false
        when (itemId) {
            R.id.nav_settings, R.id.action_settings -> {
                fragment = SettingsFragment()
                isAddToBackStack = true
            }
            R.id.nav_favorites -> {
                fragment = FavoritesFragment()
            }
            else -> fragment = MainFragment()
        }
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.flContent, fragment)
        if (isAddToBackStack)
            transaction.addToBackStack(null)
        transaction.commit()
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

}
