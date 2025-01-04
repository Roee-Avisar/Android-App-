package com.example.budgetmanager

import android.net.Uri
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.Tables.Profile
import com.example.budgetmanager.databinding.ProfileLayoutBinding

class ProfileFragment : Fragment() {

    private var _binding: ProfileLayoutBinding? = null
    private val binding get() = _binding!!
    private val userProfileViewModel: UserProfileModelView by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = ProfileLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfileViewModel.userProfileLiveData.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                binding.profileName.text = "${profile.firstName} ${profile.lastName}"
                binding.totalBudgetValue.text = profile.initialBudget.toString()
                if (!profile.imageUri.isNullOrEmpty()) {
                    binding.profileImage.setImageURI(Uri.parse(profile.imageUri))
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "No profile found.",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.createAccountFragment)
            }
        }
        binding.updateBudgetButton.setOnClickListener {
            val currentBudget = userProfileViewModel.userProfileLiveData.value?.initialBudget
            if (currentBudget != null) {
                val bundle = Bundle().apply {
                    putDouble("currentBudget", currentBudget) // מעביר את התקציב הנוכחי אם קיים
                }
                findNavController().navigate(R.id.action_profileFragment_to_addItemFragment, bundle)
            }else{
                Toast.makeText(requireContext(), "No budget available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.allItemsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allItemsFragment)
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val deleteIcon = menu.findItem(R.id.action_delete)
        deleteIcon.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showExitProfileConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit this profile")
            .setMessage("Are you sure you want to exit your profile ? all the data will be deleted")
            .setPositiveButton("Yes") { _, _ ->
                userProfileViewModel.deleteUserProfile()
                Toast.makeText(requireContext(), "All items deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
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

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
    }
}

