package com.finite.gdscphcet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.nabilmh.lottieswiperefreshlayout.LottieSwipeRefreshLayout

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private lateinit var pastEventsRecyclerView: RecyclerView
    private lateinit var upcomingEventsRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: LottieSwipeRefreshLayout
    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseRecyclerViews()

        if (homeViewModel.pastEvents.value!!.isNotEmpty()) {
            homeViewModel.loadPastEvents(binding!!)
            homeViewModel.loadUpcomingEvents(binding!!)
        } else {
            updateData()
        }

        swipeRefreshLayout = binding!!.homeSwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            updateData()
        }
    }

    private fun initialiseRecyclerViews() {
        // RecyclerView for past events
        pastEventsRecyclerView = binding!!.rvPastEvents
        pastEventsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        pastEventsRecyclerView.setHasFixedSize(true)

        // RecyclerView for upcoming events
        upcomingEventsRecyclerView = binding!!.rvUpcomingEvents
        upcomingEventsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        upcomingEventsRecyclerView.setHasFixedSize(true)
    }

    private fun updateData() {
        homeViewModel.loadPastEvents(binding!!, true)
        homeViewModel.loadUpcomingEvents(binding!!, true)
    }
}