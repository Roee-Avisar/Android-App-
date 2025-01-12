package com.example.budgetmanager.UI.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetmanager.repository.ProfileRepository
import com.example.budgetmanager.UI.fragments.CreateAccountFragment
import com.example.budgetmanager.R


class AccountCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_creation)

        val profileRepository = ProfileRepository(application)
        val existingProfile = profileRepository.getUserProfile()

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        if (existingProfile != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, CreateAccountFragment())
                    .commit()
            }
        }
    }
}
