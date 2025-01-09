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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.databinding.AddItemLayoutBinding
import com.example.budgetmanager.viewModel.ItemsViewModel
import com.example.budgetmanager.viewModel.UserProfileModelView
import java.util.Calendar

class AddItemFragment : Fragment() {

    private var _binding : AddItemLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemsViewModel by activityViewModels()
    private val profileViewModel: UserProfileModelView by activityViewModels()

    private var imageUri : Uri? = null
    private val pickImageLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()){
            binding.selectedImage.setImageURI(it)
            requireActivity().contentResolver.takePersistableUriPermission(it!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
        }

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

        val currentBudget = arguments?.getDouble("currentBudget", 0.0) ?: 0.0
        binding.currentBudgetValue.text = String.format("%.2f", currentBudget)

        binding.datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
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
                    val item = Item(
                        amount = amount,
                        description = description,
                        date = date,
                        photo = imageUri?.toString(),
                        isExpense = isExpense
                    )
                    viewModel.addItem(item, isExpense)
                    profileViewModel.updateBudget(item.amount, item.isExpense)// קריאה ל-ViewModel
                    Toast.makeText(requireContext(),
                        getString(R.string.item_added_successfully), Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment)
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(),
                        getString(R.string.invalid_amount_format), Toast.LENGTH_SHORT)
                        .show()
                }


            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
