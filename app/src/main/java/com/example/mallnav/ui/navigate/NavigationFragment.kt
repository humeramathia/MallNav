package com.example.mallnav.ui.navigate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.mallnav.R
import com.example.mallnav.data.MallSession
import com.example.mallnav.data.MockMallRepository
import com.example.mallnav.databinding.FragmentNavigationBinding

class NavigationFragment : Fragment() {
    private var _binding: FragmentNavigationBinding? = null
    private val binding get() = _binding!!
    private var onLevel2 = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val from = MockMallRepository.getStore(MallSession.startStoreId.orEmpty())
        val to = MockMallRepository.getStore(MallSession.destStoreId.orEmpty())
        binding.tvTo.text = getString(R.string.nav_to, to?.name ?: getString(R.string.app_name))
        binding.tvFrom.text = getString(R.string.nav_near, from?.name ?: "")
        binding.instruction.tvInstructionTitle.setText(R.string.nav_instruction)
        binding.instruction.tvInstructionBody.setText(R.string.nav_instruction_body)
        binding.instruction.tvNextStep.setText(R.string.nav_next_step_body)
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnFloor.setOnClickListener {
            onLevel2 = !onLevel2
            if (onLevel2) {
                binding.instruction.tvInstructionTitle.setText(R.string.nav_floor_prompt)
                binding.instruction.tvInstructionBody.text = getString(R.string.nav_to, to?.name ?: "")
                binding.instruction.tvNextStep.text = to?.name.orEmpty()
            } else {
                binding.instruction.tvInstructionTitle.setText(R.string.nav_instruction)
                binding.instruction.tvInstructionBody.setText(R.string.nav_instruction_body)
                binding.instruction.tvNextStep.setText(R.string.nav_next_step_body)
            }
        }
        binding.btnEnd.setOnClickListener {
            findNavController().navigate(
                R.id.mallHomeFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build(),
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
