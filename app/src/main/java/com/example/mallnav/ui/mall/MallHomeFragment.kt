package com.example.mallnav.ui.mall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mallnav.R
import com.example.mallnav.data.MallSession
import com.example.mallnav.data.MockMallRepository
import com.example.mallnav.databinding.FragmentMallHomeBinding
import com.example.mallnav.ui.store.StoreListFragment

class MallHomeFragment : Fragment() {
    private var _binding: FragmentMallHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMallHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mallId = arguments?.getString("mallId") ?: MallSession.mallId
        MallSession.mallId = mallId
        val mall = MockMallRepository.getMall(mallId)
        binding.tvMallName.text = mall?.name ?: getString(R.string.app_name)
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnNavigate.setOnClickListener {
            findNavController().navigate(
                R.id.storeListFragment,
                StoreListFragment.args(StoreListFragment.MODE_START),
            )
        }
        binding.cardMap.setOnClickListener { findNavController().navigate(R.id.mapFragment) }
        binding.cardFind.setOnClickListener { findNavController().navigate(R.id.directoryFragment) }
        binding.rowDirectory.setOnClickListener { findNavController().navigate(R.id.directoryFragment) }
        binding.rowFacilities.setOnClickListener {
            findNavController().navigate(
                R.id.storeListFragment,
                StoreListFragment.args(StoreListFragment.MODE_FACILITIES),
            )
        }
        binding.rowPromotions.setOnClickListener {
            findNavController().navigate(
                R.id.placeholderFragment,
                bundleOf(
                    "title" to getString(R.string.promotions_title),
                    "body" to getString(R.string.promo_one_title) + "\n" + getString(R.string.promo_two_title),
                ),
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
