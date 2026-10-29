package com.example.mallnav.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mallnav.R
import com.example.mallnav.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindRow(binding.rowAccount, R.drawable.ic_person, R.string.profile_account, R.string.profile_account_body)
        bindRow(binding.rowNotifications, R.drawable.ic_notifications, R.string.profile_notifications, R.string.profile_notifications_body)
        bindRow(binding.rowAppearance, R.drawable.ic_palette, R.string.profile_appearance, R.string.profile_appearance_body)
        bindRow(binding.rowAbout, R.drawable.ic_info, R.string.profile_about, R.string.profile_about_body)
    }

    private fun bindRow(
        row: com.example.mallnav.databinding.ItemProfileRowBinding,
        icon: Int,
        title: Int,
        body: Int,
    ) {
        row.ivIcon.setImageResource(icon)
        row.tvLabel.setText(title)
        row.root.setOnClickListener {
            findNavController().navigate(
                R.id.placeholderFragment,
                bundleOf("title" to getString(title), "body" to getString(body)),
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
