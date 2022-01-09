package com.finite.gdscphcet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.finite.gdscphcet.ui.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private var binding : FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            homeViewModel = viewModel
            homeFragment = this@HomeFragment
        }
    }

    fun openDetailActivity(id : String) {
        val action = HomeFragmentDirections.actionHomeFragmentToEventDetailActivity(id)
        findNavController().navigate(action)
    }
}