package com.example.budgetmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.budgetmanager.repository.ProfileRepository
import com.example.budgetmanager.viewModel.UserProfileModelView

class MainActivity : AppCompatActivity() {

    private lateinit var userProfileViewModel: UserProfileModelView
    private lateinit var navController: NavController
    private lateinit var profileRepository: ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // הגדרת Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // קבלת NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // התאמת Toolbar ל-Navigation Component
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // בדיקת פרופיל קיים וניווט למסך המתאים
        profileRepository = ProfileRepository(application)
        val existingProfile = profileRepository.getUserProfile()

        if (existingProfile == null) {
            navController.navigate(R.id.createAccountFragment)
        } else {
            navController.navigate(R.id.allItemsFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}

