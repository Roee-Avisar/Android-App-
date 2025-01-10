package com.example.budgetmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.budgetmanager.repository.ProfileRepository
import com.example.budgetmanager.CreateAccountFragment


class AccountCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_creation)

        // Check if the profile already exists (if yes, navigate to MainActivity)
        val profileRepository = ProfileRepository(application)
        val existingProfile = profileRepository.getUserProfile()

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        if (existingProfile != null) {
            // Navigate to the MainActivity if the account already exists
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Finish AccountCreationActivity to prevent going back
        } else {
            // Proceed with account creation flow
            if (savedInstanceState == null) {
                // Only add the fragment if this is the first time the activity is created
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, CreateAccountFragment())
                    .commit()
            }
        }
    }
}
