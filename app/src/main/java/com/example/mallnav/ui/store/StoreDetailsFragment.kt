package com.example.mallnav.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mallnav.R
import com.example.mallnav.data.MallSession
import com.example.mallnav.data.MockMallRepository
import com.example.mallnav.databinding.FragmentStoreDetailsBinding

class StoreDetailsFragment : Fragment() {
    private var _binding: FragmentStoreDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStoreDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val store = MockMallRepository.getStore(arguments?.getString("storeId").orEmpty())
        if (store == null) {
            findNavController().navigateUp()
            return
        }
        MallSession.destStoreId = store.id
        binding.tvName.text = store.name
        binding.tvCategoryBadge.text = store.category
        binding.tvCategoryBadge.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), StoreAdapter.categoryColor(store.category))
        binding.tvMeta.text = getString(R.string.label_unit, store.unit) + " · " + store.floor
        binding.tvDescription.text = store.description
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnMap.setOnClickListener { findNavController().navigate(R.id.mapFragment) }
        binding.btnNavigate.setOnClickListener {
            findNavController().navigate(R.id.storeListFragment, StoreListFragment.args(StoreListFragment.MODE_START))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
