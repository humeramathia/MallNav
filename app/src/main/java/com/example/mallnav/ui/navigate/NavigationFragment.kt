package com.example.mallnav.ui.navigate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
    private var cameraProvider: ProcessCameraProvider? = null
    private var onLevel2 = false
    private var showingMap = false

    private val requestCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) startCamera() else showFallback(R.string.camera_permission_body)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val from = MockMallRepository.getStore(MallSession.startStoreId.orEmpty())
        val to = MockMallRepository.getStore(MallSession.destStoreId.orEmpty())
        binding.tvTo.text = getString(R.string.nav_to, to?.name ?: getString(R.string.app_name))
        binding.tvFrom.text = getString(R.string.nav_near, from?.name ?: "")
        binding.labelLeft.text = from?.name ?: getString(R.string.label_from)
        binding.labelRight.text = to?.name ?: getString(R.string.label_to)
        binding.instruction.tvInstructionTitle.setText(R.string.nav_instruction)
        binding.instruction.tvInstructionBody.setText(R.string.nav_instruction_body)
        binding.instruction.tvNextStep.setText(R.string.nav_next_step_body)
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnEnableCamera.setOnClickListener { ensureCamera() }
        binding.btnToggleView.setOnClickListener { toggleMap() }
        binding.btnFloor.setOnClickListener {
            onLevel2 = !onLevel2
            if (onLevel2) {
                binding.instruction.tvInstructionTitle.setText(R.string.nav_floor_prompt)
                binding.instruction.tvInstructionBody.text = getString(R.string.nav_to, to?.name ?: "")
                binding.instruction.tvNextStep.text = to?.name.orEmpty()
                binding.ivArrow.rotation = 0f
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
        ensureCamera()
    }

    private fun ensureCamera() {
        val granted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) startCamera() else requestCamera.launch(Manifest.permission.CAMERA)
    }

    private fun startCamera() {
        val future = ProcessCameraProvider.getInstance(requireContext())
        future.addListener({
            val viewBinding = _binding ?: return@addListener
            try {
                val provider = future.get()
                cameraProvider = provider
                val preview = Preview.Builder().build().also { useCase ->
                    useCase.setSurfaceProvider(viewBinding.cameraPreview.surfaceProvider)
                }
                provider.unbindAll()
                provider.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                )
                viewBinding.cameraPreview.isVisible = true
                viewBinding.cameraFallback.isVisible = false
            } catch (_: Exception) {
                showFallback(R.string.camera_unavailable)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun showFallback(messageRes: Int) {
        val viewBinding = _binding ?: return
        viewBinding.tvFallback.setText(messageRes)
        viewBinding.cameraFallback.isVisible = true
        viewBinding.cameraPreview.isVisible = false
    }

    private fun toggleMap() {
        showingMap = !showingMap
        binding.mapFrame.isVisible = showingMap
        binding.ivArrow.isVisible = !showingMap
        binding.tvDistance.isVisible = !showingMap
        binding.labelLeft.isVisible = !showingMap
        binding.labelCenter.isVisible = !showingMap
        binding.labelRight.isVisible = !showingMap
        binding.btnToggleView.setText(
            if (showingMap) R.string.action_show_camera else R.string.action_show_map,
        )
    }

    override fun onDestroyView() {
        cameraProvider?.unbindAll()
        cameraProvider = null
        _binding = null
        super.onDestroyView()
    }
}
