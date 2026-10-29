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
import com.example.mallnav.databinding.FragmentRoutePreviewBinding

class RoutePreviewFragment : Fragment() {
    private var _binding: FragmentRoutePreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRoutePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val from = MockMallRepository.getStore(MallSession.startStoreId.orEmpty())
        val to = MockMallRepository.getStore(MallSession.destStoreId.orEmpty())
        binding.tvFrom.text = listOfNotNull(from?.name, from?.floor).joinToString(" · ")
        binding.tvTo.text = listOfNotNull(to?.name, to?.floor).joinToString(" · ")
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnStart.setOnClickListener { findNavController().navigate(R.id.navigationFragment) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
