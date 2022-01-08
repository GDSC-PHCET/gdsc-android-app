package com.finite.gdscphcet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.FragmentAboutBinding
import com.finite.gdscphcet.databinding.FragmentTeamBinding

class AboutFragment : Fragment() {

    private var binding : FragmentAboutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = FragmentAboutBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }
}