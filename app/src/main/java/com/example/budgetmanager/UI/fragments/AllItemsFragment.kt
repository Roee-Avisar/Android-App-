package com.example.budgetmanager.UI.fragments

import ItemAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.R
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.databinding.AllItemLayoutBinding
import com.example.budgetmanager.UI.viewModel.ItemsViewModel
import com.example.budgetmanager.UI.viewModel.UserProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AllItemsFragment : Fragment() {

    private var _binding: AllItemLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemsViewModel by activityViewModels()
    private val profileViewModel: UserProfileViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = AllItemLayoutBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateActionBarTitle()
        observeViewModels()

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val layoutManager = if (isLandscape) {
            GridLayoutManager(requireContext(), 2) // Grid בתצוגת רוחב
        } else {
            LinearLayoutManager(requireContext()) // Linear בתצוגת אנכי
        }
        binding.recycler.layoutManager = layoutManager

        binding.addTransactionButton.setOnClickListener {
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }

        viewModel.items.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                binding.recycler.adapter = ItemAdapter(items, object : ItemAdapter.ItemListener {
                    override fun onItemClicked(index: Int) {
                        val item = items[index]
                        viewModel.setItem(item)
                        findNavController().navigate(R.id.action_allItemsFragment_to_descriptionFragment)
                    }

                    override fun onItemLongClick(index: Int) {
                        val item = items[index]
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(getString(R.string.edit_item))
                            .setMessage(getString(R.string.do_you_want_to_edit_this_item))
                            .setPositiveButton(getString(R.string.edit)) { _, _ ->
                                viewModel.setItem(item)
                                findNavController().navigate(R.id.action_allItemsFragment_to_editItemFragment)
                            }
                            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()

            }
                })
              binding.recycler.layoutManager = LinearLayoutManager(requireContext())
            }
        }

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
                val item = (binding.recycler.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)

                showDeleteConfirmationDialog(item, viewHolder.adapterPosition)

            }

        }).attachToRecyclerView(binding.recycler)
        updateActionBarTitle()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val exitIcon = menu.findItem(R.id.action_exit)
        exitIcon.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                showDeleteAllConfirmationDialog()
                return true
            }
            R.id.action_account -> {
                findNavController().navigate(R.id.action_allItemsFragment_to_profileFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }
    private fun observeViewModels() {
        profileViewModel.budgetLiveData.observe(viewLifecycleOwner) { budget ->
            (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.budget, budget.toString())
        }
    }

    private fun showDeleteConfirmationDialog(item: Item, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_item))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_item))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                profileViewModel.revertBudget(item)
                viewModel.deleteItem(item)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                binding.recycler.adapter?.notifyItemChanged(position)

                dialog.dismiss()
            }
            .show()
    }

    private fun showDeleteAllConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_all_items))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_items))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->

                val allItems = viewModel.items.value ?: emptyList()
                allItems.forEach { item ->
                    if (item.isExpense) {
                        profileViewModel.updateBudget(item.amount, isExpense = false)
                    } else {
                        profileViewModel.updateBudget(item.amount, isExpense = true)
                    }
                }
                viewModel.deleteAll()

                Toast.makeText(requireContext(),
                    getString(R.string.all_items_deleted), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
    private fun updateActionBarTitle() {
        profileViewModel.budgetLiveData.value?.let { currentBudget ->
            (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.budget, currentBudget.toString())
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
