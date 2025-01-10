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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.databinding.AllItemLayoutBinding
import com.example.budgetmanager.viewModel.ItemsViewModel
import com.example.budgetmanager.viewModel.UserProfileViewModel
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.budgetLiveData.observe(viewLifecycleOwner) { currentBudget ->
            (activity as AppCompatActivity).supportActionBar?.title = "Budget: $${currentBudget}"
        }

        binding.addTransactionButton.setOnClickListener {
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }

        // Observing items from ViewModel
        viewModel.items.observe(viewLifecycleOwner) { items ->
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
                        viewModel.setItem(item)
                        findNavController().navigate(R.id.action_allItemsFragment_to_descriptionFragment)
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
                val item = (binding.recycler.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteItem(item)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        // If canceled, notify the adapter to redraw the item
                        binding.recycler.adapter?.notifyItemChanged(viewHolder.adapterPosition)
                        dialog.dismiss()
                    }
                    .show()
            }
        }).attachToRecyclerView(binding.recycler)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                return true
            }
            R.id.action_account -> {
                findNavController().navigate(R.id.action_allItemsFragment_to_profileFragment)
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val exitIcon = menu.findItem(R.id.action_exit)
        exitIcon.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }



    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_all_items))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_items))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteAll()
                Toast.makeText(requireContext(),
                    getString(R.string.all_items_deleted), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
