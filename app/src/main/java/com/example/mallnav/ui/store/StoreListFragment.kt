package com.example.mallnav.ui.store

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
import com.example.mallnav.data.model.Store
import com.example.mallnav.databinding.FragmentStoreListBinding

class StoreListFragment : Fragment() {
    private var _binding: FragmentStoreListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StoreAdapter
    private val mode: String get() = arguments?.getString(ARG_MODE) ?: MODE_DEST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStoreListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvTitle.text = getString(titleRes())
        binding.tvSection.isVisible = mode == MODE_START
        binding.search.etSearch.setHint(
            if (mode == MODE_START) R.string.hint_search_nearby else R.string.hint_search_stores_facilities,
        )
        adapter = StoreAdapter { store -> onStoreClicked(store) }
        binding.rvStores.adapter = adapter
        binding.rvStores.addItemDecoration(StoreAdapter.SpaceDecoration(resources.getDimensionPixelSize(R.dimen.space_sm)))
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.search.etSearch.doAfterTextChanged { applyFilter() }
        applyFilter()
    }

    private fun onStoreClicked(store: Store) {
        when (mode) {
            MODE_START -> {
                MallSession.startStoreId = store.id
                findNavController().navigate(R.id.confirmStartFragment)
            }
            MODE_DEST -> {
                MallSession.destStoreId = store.id
                findNavController().navigate(R.id.routePreviewFragment)
            }
            else -> findNavController().navigate(R.id.storeDetailsFragment, bundleOf("storeId" to store.id))
        }
    }

    private fun applyFilter() {
        val query = binding.search.etSearch.text?.toString().orEmpty()
        val mallId = MallSession.mallId
        val base = when (mode) {
            MODE_START -> MockMallRepository.getLandmarks(mallId)
            MODE_FACILITIES -> MockMallRepository.getStores(mallId).filter { it.category == "Services" }
            else -> MockMallRepository.getStores(mallId)
        }
        val filtered = base.filter {
            query.isBlank() || it.name.contains(query, true) || it.category.contains(query, true)
        }
        adapter.submitList(filtered)
        binding.tvEmpty.isVisible = filtered.isEmpty()
        binding.rvStores.isVisible = filtered.isNotEmpty()
        binding.tvEmpty.text = getString(R.string.state_no_results)
    }

    private fun titleRes(): Int = when (mode) {
        MODE_START -> R.string.starting_title
        MODE_DEST -> R.string.destination_title
        MODE_FACILITIES -> R.string.facilities_title
        else -> R.string.directory_title
    }

    override fun onDestroyView() {
        binding.rvStores.adapter = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ARG_MODE = "mode"
        const val MODE_START = "start"
        const val MODE_DEST = "dest"
        const val MODE_FACILITIES = "facilities"

        fun args(mode: String) = bundleOf(ARG_MODE to mode)
    }
}
