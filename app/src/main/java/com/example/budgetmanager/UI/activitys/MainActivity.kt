package com.example.budgetmanager.UI.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.budgetmanager.R
import com.example.budgetmanager.repository.ProfileRepository
import com.example.budgetmanager.UI.viewModel.UserProfileViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var navController: NavController
    private lateinit var profileRepository: ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
        profileRepository = ProfileRepository(application)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        userProfileViewModel.userProfileLiveData.observe(this, { existingProfile ->
            if (existingProfile == null) {
                val intent = Intent(this, AccountCreationActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val appBarConfiguration = AppBarConfiguration(navController.graph)
                setupActionBarWithNavController(navController, appBarConfiguration)

                if (savedInstanceState == null && navController.currentDestination?.id == null) {
                    navController.navigate(R.id.allItemsFragment)
                }
            }
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfiguration)

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val navState = navController.saveState()
        outState.putBundle("nav_state", navState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getBundle("nav_state")?.let { navController.restoreState(it) }
    }


}

