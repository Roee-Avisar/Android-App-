package com.example.budgetmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.databinding.AllItemLayoutBinding
import com.example.budgetmanager.viewmodel.BudgetViewModel
import kotlinx.coroutines.launch

class AllItemsFragment : Fragment() {

    private var _binding: AllItemLayoutBinding? = null
    private val binding get() = _binding!!

    private val budgetViewModel: BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = AllItemLayoutBinding.inflate(inflater, container, false)

        binding.incomeButton.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("isExpense", false) // false for income
            }
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment, bundle)
        }
        binding.expenseButton.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("isExpense", true) // true for expense
            }
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment, bundle)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Observing items from ViewModel
        budgetViewModel.getItems().observe(viewLifecycleOwner) { items ->
            if (items != null) {
                binding.recycler.adapter = ItemAdapter(items, object : ItemAdapter.ItemListener {
                    override fun onItemClicked(index: Int) {
                        if (index in items.indices) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.clicked_on, items[index].description),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.invalid_item),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onItemLongClick(index: Int) {
                        val item = items[index]
                        val bundle = bundleOf("item" to item)
                        findNavController().navigate(R.id.action_allItemsFragment_to_descriptionFragment, bundle)
                    }
                })
                binding.recycler.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        // Swipe to delete functionality
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recycler.adapter as ItemAdapter
                val item = adapter.itemAt(viewHolder.adapterPosition)

                // Show confirmation dialog
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            budgetViewModel.deleteItem(item)
                            adapter.notifyItemRemoved(viewHolder.adapterPosition)
                        }
                    }
                    .setNegativeButton("No") { _, _ ->
                        // Cancel deletion and reset the swipe
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                    .setOnCancelListener {
                        // Reset the swipe if the dialog is dismissed without choosing an option
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                    .show()
            }

        }).attachToRecyclerView(binding.recycler)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Inflate the menu resource
        inflater.inflate(R.menu.main_menu, menu)

        // Get the current budget from the ViewModel (assuming you have it in your ViewModel)
        val currentBudget = budgetViewModel.getCurrentBudget()

        // Find the MenuItem for the budget and update its title dynamically
        val budgetItem: MenuItem = menu.findItem(R.id.action_current_budget)
        budgetItem.title = "Budget: $${currentBudget}"
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // Get the current budget from ViewModel
        val currentBudget = budgetViewModel.getCurrentBudget().toString()// Get the current budget
        val budgetItem: MenuItem = menu.findItem(R.id.action_current_budget)

        // Update the title of the current budget item
        budgetItem.title = "Budget: $${currentBudget}"
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> {
                showDeleteConfirmationDialog()
                return true // Return true to indicate that the item was handled
            }
        }
        return super.onOptionsItemSelected(item) // Return the result from the parent class for other items
    }


    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_all_items))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_items))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                // Launch a coroutine to call the suspend function
                lifecycleScope.launch {
                    budgetViewModel.deleteAllItems() // Call the suspend function
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.all_items_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
