package com.example.mallnav.ui.directory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mallnav.R
import com.example.mallnav.data.MallSession
import com.example.mallnav.data.MockMallRepository
import com.example.mallnav.databinding.FragmentDirectoryBinding
import com.example.mallnav.ui.store.StoreAdapter
import com.google.android.material.chip.Chip

class DirectoryFragment : Fragment() {
    private var _binding: FragmentDirectoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StoreAdapter
    private var selectedCategory = "All"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDirectoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = StoreAdapter { store ->
            findNavController().navigate(R.id.storeDetailsFragment, bundleOf("storeId" to store.id))
        }
        binding.rvStores.adapter = adapter
        binding.rvStores.addItemDecoration(StoreAdapter.SpaceDecoration(resources.getDimensionPixelSize(R.dimen.space_sm)))
        binding.search.etSearch.setHint(R.string.hint_search_stores)
        MockMallRepository.getCategories().forEachIndexed { index, label ->
            val chip = layoutInflater.inflate(R.layout.item_category_chip, binding.chipGroup, false) as Chip
            chip.id = View.generateViewId()
            chip.text = label
            chip.isChecked = index == 0
            binding.chipGroup.addView(chip)
        }
        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            selectedCategory = group.findViewById<Chip>(checkedId).text.toString()
            applyFilter()
        }
        binding.search.etSearch.doAfterTextChanged { applyFilter() }
        applyFilter()
    }

    private fun applyFilter() {
        val query = binding.search.etSearch.text?.toString().orEmpty()
        val stores = MockMallRepository.getStores(MallSession.mallId).filter { store ->
            val categoryOk = selectedCategory == "All" || store.category == selectedCategory
            val queryOk = query.isBlank() ||
                store.name.contains(query, true) ||
                store.category.contains(query, true)
            categoryOk && queryOk
        }
        adapter.submitList(stores)
        binding.tvEmpty.isVisible = stores.isEmpty()
        binding.rvStores.isVisible = stores.isNotEmpty()
        binding.tvEmpty.text = getString(
            if (query.isBlank()) R.string.state_empty else R.string.state_no_results,
        )
    }

    override fun onDestroyView() {
        binding.rvStores.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
