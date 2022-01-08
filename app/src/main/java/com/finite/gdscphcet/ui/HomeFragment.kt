package com.finite.gdscphcet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.finite.gdscphcet.databinding.FragmentTeamBinding

class HomeFragment : Fragment() {

    private var binding : FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }
}