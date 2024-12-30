package com.example.budgetmanager

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
    }
}

