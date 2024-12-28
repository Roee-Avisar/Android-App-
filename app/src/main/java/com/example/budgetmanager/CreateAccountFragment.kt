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
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.databinding.CreateAccountLayoutBinding


class CreateAccountFragment : Fragment() {

    private var _binding: CreateAccountLayoutBinding? = null
    private val binding get() = _binding!!

    private var imageUri : Uri? = null

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

        // Set up listeners using binding
        binding.uploadImageButton.setOnClickListener {
            // Placeholder: Implement image upload logic
//            Toast.makeText(requireContext(), "Upload Image Clicked", Toast.LENGTH_SHORT).show()

                pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.saveUserButton.setOnClickListener {
            val firstName = binding.firstNameInput.text.toString().trim()
            val lastName = binding.lastNameInput.text.toString().trim()
            val initialBudget = binding.initialBudgetInput.text.toString().trim()

            // Validate input
            if (firstName.isEmpty() || lastName.isEmpty() || initialBudget.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Placeholder: Save user data logic
//                Toast.makeText(
//                    requireContext(),
//                    "User Saved: $firstName $lastName, Budget: $initialBudget",
//                    Toast.LENGTH_LONG
//                ).show()
                val bundle = Bundle().apply {
                    putString("firstName", firstName)
                    putString("lastName", lastName)
                    putString("initialBudget", initialBudget)
                    imageUri?.let { putString("imageUri", it.toString()) }
                }
                findNavController().navigate(R.id.action_createAccountFragment_to_profileFragment, bundle)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
