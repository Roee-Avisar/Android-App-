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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.databinding.AllItemLayoutBinding

class AllItemsFragment : Fragment() {

    private var _binding: AllItemLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemsViewModel by activityViewModels()

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


        // Observing items from ViewModel
        viewModel.items.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                binding.recycler.adapter = ItemAdapter(items, object : ItemAdapter.ItemListener {
                    override fun onItemClicked(index: Int) {
                        Toast.makeText(requireContext(), "Clicked on: ${items[index].description}", Toast.LENGTH_SHORT).show()
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
                viewModel.deleteItem(item)
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
            .setTitle("Delete All Items")
            .setMessage("Are you sure you want to delete all items?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAll()
                Toast.makeText(requireContext(), "All items deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
