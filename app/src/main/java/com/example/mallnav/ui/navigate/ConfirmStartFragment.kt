package com.example.mallnav.ui.navigate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mallnav.R
import com.example.mallnav.data.MallSession
import com.example.mallnav.data.MockMallRepository
import com.example.mallnav.databinding.FragmentConfirmStartBinding
import com.example.mallnav.ui.store.StoreListFragment

class ConfirmStartFragment : Fragment() {
    private var _binding: FragmentConfirmStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentConfirmStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val store = MockMallRepository.getStore(MallSession.startStoreId.orEmpty())
        binding.tvName.text = store?.name ?: getString(R.string.app_name)
        binding.tvFloor.text = store?.floor.orEmpty()
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnChange.setOnClickListener { findNavController().navigateUp() }
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.storeListFragment, StoreListFragment.args(StoreListFragment.MODE_DEST))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
