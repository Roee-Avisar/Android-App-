package com.example.budgetmanager

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.databinding.ItemDescriptionLayoutBinding
import com.example.budgetmanager.viewmodel.BudgetViewModel

class DescriptionFragment : Fragment() {


    var _binding: ItemDescriptionLayoutBinding? = null
    val binding get() = _binding!!


    val viewModel : BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemDescriptionLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the chosenItem LiveData
        viewModel.chosenItem.observe(viewLifecycleOwner) { item ->
            // Safely handle item being null or not
            item?.let {
                binding.itemDescription.text = it.description ?: "No description"
                binding.itemValue.text = it.amount.toString()
                binding.itemDate.text = it.date ?: "No date"

                if (!it.photo.isNullOrEmpty()) {
                    val imageUri = Uri.parse(it.photo)
                    binding.itemPhoto.setImageURI(imageUri)
                } else {
                    binding.itemPhoto.setImageResource(R.drawable.ic_launcher_foreground)
                }
            } ?: run {
                // If item is null, handle the UI as needed
                binding.itemDescription.text = "No description"
                binding.itemValue.text = "0.0"
                binding.itemDate.text = "No date"
                binding.itemPhoto.setImageResource(R.drawable.ic_launcher_foreground)  // Fallback image
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}