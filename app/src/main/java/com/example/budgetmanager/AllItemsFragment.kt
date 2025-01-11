package com.example.budgetmanager

import android.net.Uri
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
    private val userProfileViewModel: UserProfileModelView by activityViewModels()

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

                            val item = items[index]
                            viewModel.setItem(item)
                            findNavController().navigate(R.id.action_allItemsFragment_to_descriptionFragment)

                    }

                    override fun onItemLongClick(index: Int) {
                       //need to add
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
        binding.toProfileBtn.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle("move to profile page")
                .setMessage(" you want move to profile page")
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    findNavController().navigate(R.id.action_allItemsFragment_to_profileFragment)
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()

        }



        userProfileViewModel.userProfileLiveData.observe(viewLifecycleOwner){profile ->
            if (profile != null) {
                if (!profile.imageUri.isNullOrEmpty()) {
                    binding.toProfileBtn.setImageURI(Uri.parse(profile.imageUri))
                }
            }
        }






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
