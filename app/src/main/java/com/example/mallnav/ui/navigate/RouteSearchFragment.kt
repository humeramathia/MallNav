package com.example.mallnav.ui.navigate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mallnav.R
import com.example.mallnav.data.MallSession
import com.example.mallnav.data.MockMallRepository
import com.example.mallnav.data.model.Store
import com.example.mallnav.databinding.FragmentRouteSearchBinding
import com.example.mallnav.ui.store.StoreAdapter

class RouteSearchFragment : Fragment() {
    private var _binding: FragmentRouteSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StoreAdapter

    private enum class Field { PICKUP, DROPOFF, STOP }

    private var activeField = Field.PICKUP
    private var suppressChange = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRouteSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = StoreAdapter { store -> onStoreClicked(store) }
        binding.rvStores.adapter = adapter
        binding.rvStores.addItemDecoration(StoreAdapter.SpaceDecoration(resources.getDimensionPixelSize(R.dimen.space_sm)))
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnAddStop.setOnClickListener {
            binding.stopField.isVisible = true
            binding.btnAddStop.isVisible = false
            setActiveField(Field.STOP)
            binding.etStop.requestFocus()
        }
        binding.btnContinue.setOnClickListener {
            if (bothEndsSelected()) findNavController().navigate(R.id.routePreviewFragment)
        }

        bindField(binding.etPickup, Field.PICKUP)
        bindField(binding.etDropoff, Field.DROPOFF)
        bindField(binding.etStop, Field.STOP)

        restoreSelectedNames()
        val focusDropoff = arguments?.getBoolean(ARG_FOCUS_DROPOFF) == true && MallSession.destStoreId != null
        setActiveField(if (focusDropoff) Field.DROPOFF else Field.PICKUP)
        if (focusDropoff) binding.etDropoff.requestFocus() else binding.etPickup.requestFocus()
        applyFilter()
        updateContinue()
    }

    private fun bindField(field: android.widget.EditText, which: Field) {
        field.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setActiveField(which)
                applyFilter()
            }
        }
        field.doAfterTextChanged { text ->
            if (suppressChange) return@doAfterTextChanged
            clearSelection(which)
            if (activeField == which) applyFilter()
            updateContinue()
        }
        field.setOnEditorActionListener { _, actionId, _ ->
            actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_NEXT
        }
    }

    private fun restoreSelectedNames() {
        suppressChange = true
        MockMallRepository.getStore(MallSession.startStoreId.orEmpty())?.let {
            binding.etPickup.setText(it.name)
        }
        MockMallRepository.getStore(MallSession.destStoreId.orEmpty())?.let {
            binding.etDropoff.setText(it.name)
        }
        suppressChange = false
    }

    private fun setActiveField(field: Field) {
        activeField = field
        binding.pickupField.setBackgroundResource(
            if (field == Field.PICKUP) R.drawable.bg_location_field_active else R.drawable.bg_location_field_inactive,
        )
        binding.dropoffField.setBackgroundResource(
            if (field == Field.DROPOFF) R.drawable.bg_location_field_active else R.drawable.bg_location_field_inactive,
        )
        binding.stopField.setBackgroundResource(
            if (field == Field.STOP) R.drawable.bg_location_field_active else R.drawable.bg_location_field_inactive,
        )
        binding.tvSection.setText(
            if (field == Field.PICKUP) R.string.label_nearby_stores else R.string.destination_subtitle,
        )
    }

    private fun onStoreClicked(store: Store) {
        suppressChange = true
        when (activeField) {
            Field.PICKUP -> {
                MallSession.startStoreId = store.id
                binding.etPickup.setText(store.name)
                binding.etDropoff.requestFocus()
                setActiveField(Field.DROPOFF)
            }
            Field.DROPOFF -> {
                MallSession.destStoreId = store.id
                binding.etDropoff.setText(store.name)
            }
            Field.STOP -> binding.etStop.setText(store.name)
        }
        suppressChange = false
        applyFilter()
        updateContinue()
        if (bothEndsSelected() && activeField != Field.STOP) {
            findNavController().navigate(R.id.routePreviewFragment)
        }
    }

    private fun clearSelection(field: Field) {
        when (field) {
            Field.PICKUP -> if (binding.etPickup.text?.toString() != selectedName(MallSession.startStoreId)) {
                MallSession.startStoreId = null
            }
            Field.DROPOFF -> if (binding.etDropoff.text?.toString() != selectedName(MallSession.destStoreId)) {
                MallSession.destStoreId = null
            }
            Field.STOP -> Unit
        }
    }

    private fun selectedName(id: String?): String? = id?.let { MockMallRepository.getStore(it)?.name }

    private fun bothEndsSelected(): Boolean =
        MallSession.startStoreId != null && MallSession.destStoreId != null

    private fun updateContinue() {
        binding.btnContinue.isEnabled = bothEndsSelected()
    }

    private fun applyFilter() {
        val query = when (activeField) {
            Field.PICKUP -> binding.etPickup.text
            Field.DROPOFF -> binding.etDropoff.text
            Field.STOP -> binding.etStop.text
        }?.toString().orEmpty()
        val selectedName = when (activeField) {
            Field.PICKUP -> selectedName(MallSession.startStoreId)
            Field.DROPOFF -> selectedName(MallSession.destStoreId)
            Field.STOP -> null
        }
        val search = if (query == selectedName) "" else query
        val mallId = MallSession.mallId
        val base = if (activeField == Field.PICKUP) {
            MockMallRepository.getLandmarks(mallId)
        } else {
            MockMallRepository.getStores(mallId)
        }
        val filtered = base.filter {
            search.isBlank() || it.name.contains(search, true) || it.category.contains(search, true)
        }
        adapter.submitList(filtered)
        binding.tvEmpty.isVisible = filtered.isEmpty()
        binding.rvStores.isVisible = filtered.isNotEmpty()
        binding.tvEmpty.text = getString(R.string.state_no_results)
    }

    override fun onDestroyView() {
        binding.rvStores.adapter = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ARG_FOCUS_DROPOFF = "focusDropoff"
    }
}
