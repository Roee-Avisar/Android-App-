package com.example.budgetmanager

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.UI.viewModel.ItemsViewModel
import com.example.budgetmanager.UI.viewModel.UserProfileViewModel
import com.example.budgetmanager.databinding.EditItemFragmentBinding
import java.util.Calendar

class EditItemFragment : Fragment() {

    private var _binding: EditItemFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemsViewModel by activityViewModels()
    private val profileViewModel: UserProfileViewModel by activityViewModels()
    private var itemToUpdate: Item? = null

    private var imageUri: Uri? = null
    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.selectedImage.setImageURI(it)
            requireActivity().contentResolver.takePersistableUriPermission(it!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = EditItemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        profileViewModel.budgetLiveData.observe(viewLifecycleOwner) { currentBudget ->
            (activity as AppCompatActivity).supportActionBar?.title = "Budget: $${currentBudget}"
        }

        // Populate fields with the current item's data
        viewModel.chosenItem.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.amountInput.setText(it.amount.toString())
                binding.descriptionInput.setText(it.description)
                binding.datePicker.setText(it.date)
                // Set image if available
                it.photo?.let { photoUri ->
                    binding.selectedImage.setImageURI(Uri.parse(photoUri))
                    imageUri = Uri.parse(photoUri)
                }
                binding.expenseRadio.isChecked = it.isExpense
                itemToUpdate = it
            }
        }

        binding.datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.datePicker.setText(date)
            }, year, month, day)

            datePickerDialog.show()
        }

        binding.takePhotoButton.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.confirmTransactionButton.setOnClickListener {
            val amountText = binding.amountInput.text.toString().trim()
            val description = binding.descriptionInput.text.toString().trim()
            val date = binding.datePicker.text.toString()
            val isExpense = binding.expenseRadio.isChecked

            if (amountText.isEmpty() || description.isEmpty() || date.isEmpty()) {
                Toast.makeText(requireContext(),
                    getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT)
                    .show()
            } else {
                try {
                    val amount = amountText.toDouble()
                    val updatedItem = Item(
                        amount = amount,
                        description = description,
                        date = date,
                        photo = imageUri?.toString(),
                        isExpense = isExpense
                    )
                    if (updatedItem != null) {
                        viewModel.updateItem(updatedItem, itemToUpdate!!)
                        profileViewModel.updateBudget(updatedItem.amount, updatedItem.isExpense)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.item_updated_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(),
                        getString(R.string.invalid_amount_format), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()  // Navigate back without saving
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}