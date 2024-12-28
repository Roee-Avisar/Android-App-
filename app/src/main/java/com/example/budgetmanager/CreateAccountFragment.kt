package com.example.budgetmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.budgetmanager.databinding.CreateAccountLayoutBinding

class CreateAccountFragment : Fragment() {

    private var _binding: CreateAccountLayoutBinding? = null
    private val binding get() = _binding!!

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
            Toast.makeText(requireContext(), "Upload Image Clicked", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(
                    requireContext(),
                    "User Saved: $firstName $lastName, Budget: $initialBudget",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
