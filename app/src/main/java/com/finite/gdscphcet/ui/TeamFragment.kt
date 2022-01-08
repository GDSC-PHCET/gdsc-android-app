package com.finite.gdscphcet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.FragmentTeamBinding

class TeamFragment : Fragment() {

    private var binding : FragmentTeamBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = FragmentTeamBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

}