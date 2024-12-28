package com.example.budgetmanager

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.databinding.ProfileLayoutBinding
class ProfileFragment : Fragment() {

    private var _binding: ProfileLayoutBinding? = null
    private val binding get() = _binding!!

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

        val firstName = arguments?.getString("firstName")
        val lastName = arguments?.getString("lastName")
        val initialBudget = arguments?.getString("initialBudget")
        val imageUri = arguments?.getString("imageUri")

        // עדכון הנתונים ב-Layout
        binding.profileName.text = "$firstName $lastName"
        binding.totalBudgetValue.text = initialBudget

        if (imageUri != null) {
            binding.profileImage.setImageURI(Uri.parse(imageUri))
        }


        binding.updateBudgetButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("currentBudget", initialBudget) // העבר את התקציב הנוכחי
            }
            findNavController().navigate(R.id.action_profileFragment_to_addItemFragment, bundle)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
