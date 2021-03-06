package com.adityakamble49.wordlist.ui.main

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.adityakamble49.wordlist.R
import com.adityakamble49.wordlist.ui.about.AboutActivity
import com.adityakamble49.wordlist.ui.common.BaseInjectableActivity
import com.adityakamble49.wordlist.ui.list.WordListFragment
import com.adityakamble49.wordlist.ui.marketplace.MarketplaceFragment
import com.adityakamble49.wordlist.ui.search.SearchFragment
import com.adityakamble49.wordlist.ui.settings.SettingsFragment
import com.adityakamble49.wordlist.ui.word.WordActivity
import com.adityakamble49.wordlist.utils.invisible
import com.adityakamble49.wordlist.utils.replaceFragment
import com.adityakamble49.wordlist.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class MainActivity : BaseInjectableActivity(), MainContract.View, View.OnClickListener {

    // Dagger Injected Fields
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var presenter: MainContract.Presenter

    // View Fields
    private var loadingDialog: ProgressDialog? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    // Other Fields
    private lateinit var uiHandler: Handler
    @IdRes private var selectedNavigationItem = R.id.nav_wordlist
    @IdRes private var previousNavigationItem = R.id.nav_wordlist
    @IdRes private val ID_NAV_WORD_LIST = R.id.nav_wordlist
    @IdRes private val ID_NAV_MARKETPLACE = R.id.nav_marketplace
    @IdRes private val ID_NAV_SETTINGS = R.id.nav_settings
    @IdRes private val ID_NAV_ABOUT = R.id.nav_about


    /*
     * Lifecycle Functions
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create UI Handler
        uiHandler = Handler()
        observeFragmentBackStack()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onBackPressed() {
        if (drawer_layout_main.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_main.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
    }

    /*
     * Listener Functions
     */

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_learn_words -> presenter.onClickLearnWords()
            R.id.fab_practice_words -> presenter.onClickPracticeWords()
        }
    }

    /*
     * Helper Functions
     */

    override fun getLayoutId() = R.layout.activity_main

    override fun bindView() {
        // Setup Support Action Bar
        setSupportActionBar(toolbar_main)

        if (savedInstanceState == null) {
            updateTitle(getString(R.string.label_word_list))
        }

        // Setup Navigation Drawer
        navigation_main.setNavigationItemSelectedListener { item ->
            selectedNavigationItem = item.itemId
            drawer_layout_main.closeDrawer(GravityCompat.START)
            true
        }
        drawerToggle = object : ActionBarDrawerToggle(this, drawer_layout_main, toolbar_main,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                if (selectedNavigationItem != previousNavigationItem) {
                    handleNavigationDrawerSelection(selectedNavigationItem)
                    previousNavigationItem = selectedNavigationItem
                }
            }
        }
        drawer_layout_main.addDrawerListener(drawerToggle as ActionBarDrawerToggle)
        drawerToggle?.syncState()

        // Setup Task FAB
        fab_learn_words.setOnClickListener(this)
        fab_practice_words.setOnClickListener(this)
    }

    private fun updateTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun observeFragmentBackStack() {
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)
            if (currentFragment != null) {
                updateTitleAndDrawer(currentFragment)
            }
        }
    }

    private fun updateTitleAndDrawer(currentFragment: Fragment) {
        showFAB(false)
        toggleDrawerToggleIndicator(true)
        when (currentFragment.javaClass.name) {
            WordListFragment::class.java.name -> {
                updateTitle(getString(R.string.app_name))
                showFAB(true)
                resetNavigationHistory(ID_NAV_WORD_LIST)
            }
            MarketplaceFragment::class.java.name -> {
                updateTitle(getString(R.string.marketplace_title))
                resetNavigationHistory(ID_NAV_MARKETPLACE)
            }
            SettingsFragment::class.java.name -> {
                updateTitle(getString(R.string.label_settings))
                resetNavigationHistory(ID_NAV_SETTINGS)
            }
            SearchFragment::class.java.name -> {
                toggleDrawerToggleIndicator(false)
                updateTitle(getString(R.string.label_search))
            }
        }
    }

    private fun handleNavigationDrawerSelection(@IdRes itemId: Int) {
        when (itemId) {
            R.id.nav_wordlist -> presenter.onClickWordList()
            R.id.nav_marketplace -> presenter.onClickMarketplace()
            R.id.nav_settings -> presenter.onClickSettings()
            R.id.nav_about -> presenter.onClickAbout()
        }
    }

    override fun initializePresenter() {
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(
                MainActivityViewModel::class.java)
        presenter.setViewModel(viewModel)
        presenter.initialize()
    }

    override fun getActivityPresenter() = presenter

    private fun loadDefaultFragment() {
        replaceFragment(WordListFragment.newInstance(), R.id.main_container)
    }

    fun showFAB(show: Boolean) {
        if (show) {
            fab_new_task.visible()
        } else {
            fab_new_task.invisible()
        }
    }

    override fun showLoadingDialog(toShow: Boolean, title: String, content: String) {
        if (toShow) {
            buildLoadingDialog(title, content)?.show()
        } else {
            loadingDialog?.dismiss()
        }
    }

    private fun buildLoadingDialog(title: String, content: String): ProgressDialog? {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(this)
        }
        loadingDialog?.let {
            it.setTitle(title)
            it.setMessage(content)
        }
        return loadingDialog
    }

    override fun dataInitialized() {
        loadDefaultFragment()
    }

    private fun toggleDrawerToggleIndicator(toShow: Boolean) {
        drawerToggle?.isDrawerIndicatorEnabled = toShow
    }

    override fun startWordActivity(wordActivityMode: WordActivity.Companion.WordActivityMode) {
        val wordActivityIntent = Intent(this, WordActivity::class.java)
        wordActivityIntent.putExtra(WordActivity.IE_KEY_WORD_ACTIVITY_MODE,
                wordActivityMode.ordinal)
        startActivity(wordActivityIntent)
        fab_new_task.collapse()
    }

    override fun openWordList() {
        replaceFragment(WordListFragment.newInstance(), R.id.main_container)
    }

    override fun openMarketplace() {
        replaceFragment(MarketplaceFragment.newInstance(), R.id.main_container)
    }

    override fun openSettings() {
        replaceFragment(SettingsFragment.newInstance(), R.id.main_container)
    }

    override fun openAbout() {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    override fun resetNavigationHistory(@IdRes navigationItem: Int) {
        selectedNavigationItem = navigationItem
        previousNavigationItem = navigationItem
    }

    override fun handleFinishActivity() {
        if (fab_new_task.isExpanded) {
            fab_new_task.collapse()
        } else {
            finish()
        }
    }
}
