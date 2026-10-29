package com.example.mallnav.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mallnav.R
import com.example.mallnav.data.MallSession
import com.example.mallnav.data.MockMallRepository
import com.example.mallnav.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mallRepository = MockMallRepository
    private lateinit var mallAdapter: MallAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mallAdapter = MallAdapter { mall ->
            MallSession.mallId = mall.id
            findNavController().navigate(R.id.mallHomeFragment, bundleOf("mallId" to mall.id))
        }
        binding.search.etSearch.setHint(R.string.hint_search_malls)
        binding.rvMalls.adapter = mallAdapter
        binding.rvMalls.addItemDecoration(SpaceDecoration())
        binding.search.etSearch.doAfterTextChanged { text ->
            render(mallsForQuery(text?.toString().orEmpty()))
        }
        render(mallsForQuery(binding.search.etSearch.text?.toString().orEmpty()))
    }

    override fun onDestroyView() {
        binding.rvMalls.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun mallsForQuery(query: String): HomeUiState {
        val malls = mallRepository.getMalls()
        if (malls.isEmpty()) return HomeUiState.Empty
        val filtered = malls.filter {
            it.name.contains(query, true) || it.city.contains(query, true)
        }
        return if (filtered.isEmpty()) HomeUiState.NoResults(query) else HomeUiState.Results(filtered)
    }

    private fun render(state: HomeUiState) {
        binding.progress.isVisible = state is HomeUiState.Loading
        binding.tvEmpty.isVisible = state is HomeUiState.Empty || state is HomeUiState.NoResults
        binding.rvMalls.isVisible = state is HomeUiState.Results
        when (state) {
            HomeUiState.Loading -> mallAdapter.submitList(emptyList())
            HomeUiState.Empty -> {
                binding.tvEmpty.text = getString(R.string.state_empty)
                mallAdapter.submitList(emptyList())
            }
            is HomeUiState.NoResults -> {
                binding.tvEmpty.text = getString(R.string.state_no_results)
                mallAdapter.submitList(emptyList())
            }
            is HomeUiState.Results -> mallAdapter.submitList(state.malls)
        }
    }

    private inner class SpaceDecoration : RecyclerView.ItemDecoration() {
        private val spacing = resources.getDimensionPixelSize(R.dimen.space_sm)
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (parent.getChildAdapterPosition(view) > 0) outRect.top = spacing
        }
    }
}
