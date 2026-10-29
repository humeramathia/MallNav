package com.example.mallnav.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mallnav.R
import com.example.mallnav.data.MallSession
import com.example.mallnav.data.MockMallRepository
import com.example.mallnav.databinding.FragmentMapBinding

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var scale = 1f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mall = MockMallRepository.getMall(MallSession.mallId)
        binding.tvMallName.text = mall?.name ?: getString(R.string.state_select_mall)
        binding.btnLocate.isEnabled = false
        binding.btnZoomIn.setOnClickListener {
            scale = (scale + 0.15f).coerceAtMost(1.6f)
            binding.floorPlan.root.scaleX = scale
            binding.floorPlan.root.scaleY = scale
        }
        binding.btnZoomOut.setOnClickListener {
            scale = (scale - 0.15f).coerceAtLeast(0.85f)
            binding.floorPlan.root.scaleX = scale
            binding.floorPlan.root.scaleY = scale
        }
        binding.floors.floorSelector.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            val nikeOnL2 = checkedId == R.id.btn_floor_l2 || checkedId == R.id.btn_floor_l3
            binding.floorPlan.blockNike.alpha = if (nikeOnL2) 1f else 0.35f
            binding.floorPlan.blockWoolworths.alpha = if (nikeOnL2) 0.35f else 1f
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
