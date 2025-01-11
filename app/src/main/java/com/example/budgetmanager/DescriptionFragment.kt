package com.example.budgetmanager

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.room.Index
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.databinding.ItemDescriptionLayoutBinding

class DescriptionFragment : Fragment() {


    var _binding: ItemDescriptionLayoutBinding? = null
    val binding get() = _binding!!

    val viewModel : ItemsViewModel by activityViewModels()

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
        viewModel.chosenItem.observe(viewLifecycleOwner) {
            binding.itemDescription.text = it.description
            binding.itemValue.text = it.amount.toString()
            binding.itemDate.text = it.date
            if (!it.photo.isNullOrEmpty()) {
                val imageUri = Uri.parse(it.photo)
                binding.itemPhoto.setImageURI(imageUri)
            } else {
                binding.itemPhoto.setImageResource(R.drawable.ic_launcher_foreground)
            }
            binding.deleteTransactionBtn.setOnClickListener {
                val item=viewModel.chosenItem.value
                AlertDialog.Builder(requireContext())
                    .setTitle("delete Transaction")
                    .setMessage("are you sure you want delete this transaction")
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        if (item != null) {
                            viewModel.deleteItem(item)
                        }
                        Toast.makeText(requireContext(),
                            getString(R.string.all_items_deleted), Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(getString(R.string.no), null)
                    .show()

                findNavController().navigate(R.id.action_descriptionFragment_to_allItemsFragment)
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}