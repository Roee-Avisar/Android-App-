package com.example.budgetmanager.UI.fragments

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.budgetmanager.R
import com.example.budgetmanager.databinding.ProfileLayoutBinding
import com.example.budgetmanager.UI.viewModel.UserProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: ProfileLayoutBinding? = null
    private val binding get() = _binding!!
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            Log.d("ProfileFragment", "Loaded landscape layout")
        } else {
            Log.d("ProfileFragment", "Loaded portrait layout")
        }
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Budget Manager Profile"
        _binding = ProfileLayoutBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userProfileViewModel.userProfileLiveData.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                binding.profileName?.text = "${profile.firstName} ${profile.lastName}"
                binding.totalBudgetValue.text = userProfileViewModel.budgetLiveData.value.toString()
                binding.expenseValue.text = userProfileViewModel.expensesLiveData.value.toString()
                binding.incomeValue.text = userProfileViewModel.incomeLiveData.value.toString()
                if (!profile.imageUri.isNullOrEmpty()) {
                    Glide.with(binding.root.context)
                        .load(Uri.parse(profile.imageUri)) // Load the image from the URI
                        .placeholder(R.drawable.ic_launcher_background)
                        .circleCrop()// Default image while loading
                        .into(binding.profileImage)
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_profile_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val deleteIcon = menu.findItem(R.id.action_delete)
        val profileIcon = menu.findItem(R.id.action_account)
        profileIcon.isVisible = false
        deleteIcon.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showExitProfileConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.exit_this_profile))
            .setMessage(getString(R.string.are_you_sure_you_want_to_exit_your_profile_all_the_data_will_be_deleted))
            .setPositiveButton(R.string.yes) { _, _ ->
                userProfileViewModel.deleteUserProfile()
                Toast.makeText(requireContext(), R.string.all_items_deleted, Toast.LENGTH_SHORT).show()

            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_exit -> {
                showExitProfileConfirmationDialog() // העלאת דיאלוג לאישור
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // העברת נתונים לתצוגת landscape
            Log.d("ProfileFragment", "Landscape mode detected")
        } else {
            // העברת נתונים לתצוגת portrait
            Log.d("ProfileFragment", "Portrait mode detected")
        }
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
    }
}

