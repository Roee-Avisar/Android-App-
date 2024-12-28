package com.example.budgetmanager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.databinding.AddItemLayoutBinding

class AddItemFragment : Fragment() {

    private var _binding : AddItemLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AddItemLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentBudget = arguments?.getString("currentBudget")
        binding.currentBudgetValue.text = "$currentBudget"

        binding.confirmTransactionButton.setOnClickListener {
            val amount = binding.amountInput.text.toString().trim()
            val description = binding.descriptionInput.text.toString().trim()

            // Validate input
            if (amount.isEmpty() || description.isEmpty() ) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle().apply {
                    putString("amount", amount)
                    putString("description", description)
                }

                findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
