package com.example.budgetmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.Tables.Profile
import com.example.budgetmanager.databinding.CreateAccountLayoutBinding
import com.example.budgetmanager.viewModel.UserProfileModelView


class CreateAccountFragment : Fragment() {

    private var _binding: CreateAccountLayoutBinding? = null
    private val binding get() = _binding!!

    private var imageUri : Uri? = null
    private val userProfileViewModel: UserProfileModelView by activityViewModels()

    private val pickImageLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()){
            binding.profileImage.setImageURI(it)
            requireActivity().contentResolver.takePersistableUriPermission(it!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateAccountLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.uploadImageButton.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }
        binding.saveUserButton.setOnClickListener {
            val firstName = binding.firstNameInput.text.toString().trim()
            val lastName = binding.lastNameInput.text.toString().trim()
            val initialBudgetString = binding.initialBudgetInput.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(requireContext(), R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val initialBudget = initialBudgetString.toDoubleOrNull()
            if (initialBudget == null) {
                Toast.makeText(requireContext(),
                    getString(R.string.please_enter_a_valid_budget), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val userProfile =
                Profile(
                    firstName = firstName,
                    lastName = lastName,
                    initialBudget = initialBudget,
                    imageUri = imageUri?.toString()
                )
            userProfileViewModel.insertUserProfile(userProfile)

            Toast.makeText(requireContext(),
                getString(R.string.profile_created_successfully), Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_createAccountFragment_to_profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

