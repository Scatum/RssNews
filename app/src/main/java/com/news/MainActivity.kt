package com.news

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavController
import com.news.app.AppStateListener
import com.news.extension.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            try {
                // safe call
                // need for bug navigationcontroller
                setContentView(R.layout.activity_main)
                setupNavController()
            } catch (ex: Exception) {
                Log.e("Exception", " onSetContentView ${ex.message}")
            }
        }

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupNavController()
    }


    override fun onDestroy() {
        finish()// need for bug navigationcontroller
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun setupNavController() {
        val navGraphIds = listOf(
            R.navigation.feed,
            R.navigation.archive,
            R.navigation.settings
        )

        // Setup the bottom navigation view with a list of navigation graphs
        currentNavController = bottomNavigationView?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.navHostFragment,
            intent = intent
        )

    }

}
