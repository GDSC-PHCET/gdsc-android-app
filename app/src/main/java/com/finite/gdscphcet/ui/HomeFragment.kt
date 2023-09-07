package com.finite.gdscphcet.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.finite.gdscphcet.R
import com.finite.gdscphcet.adapters.PastEventsAdapter
import com.finite.gdscphcet.adapters.UpcomingEventsAdapter
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.finite.gdscphcet.model.PastEventModel
import com.finite.gdscphcet.model.UpcomingEventModel
import com.finite.gdscphcet.repository.PastEventRepo
import com.finite.gdscphcet.repository.UpcomingEventRepo
import com.finite.gdscphcet.ui.viewModel.HomeViewModel
import com.finite.gdscphcet.ui.viewModel.PastEvents
import com.finite.scrapingpractise.model.PastEvent
import com.finite.scrapingpractise.model.UpcomingEvent
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.math.log

class HomeFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var pastEventsRecyclerView: RecyclerView
    private lateinit var pastEventsList: ArrayList<PastEvent>
    private lateinit var upcomingEventsRecyclerView: RecyclerView
    private lateinit var upcomingEventsList: ArrayList<UpcomingEventModel>
    private lateinit var upcomingPlaceHolder: LinearLayout
    private lateinit var upcomingShimmer: ShimmerFrameLayout
    private lateinit var pastShimmer: ShimmerFrameLayout

//    private val url = "https://gdsc.community.dev/pillai-hoc-college-of-engineering-and-technology-navi-mumbai/"
    private val url = "https://gdsc.community.dev/dy-patil-college-of-engineering-pune/"
//      private val url = "https://gdsc.community.dev/mody-university-of-science-and-technology-laxmangarh/"
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

        binding?.apply {
            homeViewModel = viewModel
            homeFragment = this@HomeFragment
        }

        upcomingShimmer = binding!!.shimmerEffectUpcomingRv
        pastShimmer = binding!!.shimmerEffectPastRv
        upcomingPlaceHolder = binding!!.upcomingEventsPlaceholderLayout

        upcomingShimmer.startShimmer()
        pastShimmer.startShimmer()

//        binding?.verifyButton?.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToCertificateActivity()
//            findNavController().navigate(action)
//        }

        pastEventsRecyclerView = binding!!.rvPastEvents
        pastEventsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        pastEventsRecyclerView.setHasFixedSize(true)


        upcomingEventsRecyclerView = binding!!.rvUpcomingEvents
        upcomingEventsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        upcomingEventsRecyclerView.setHasFixedSize(true)
        upcomingEventsList = arrayListOf()


        CoroutineScope(Dispatchers.Main).launch {
            newgetPastEvents()
            newgetUpcomingEvents()
        }
    }

    private suspend fun newgetPastEvents() {

        var pastEvents = mutableListOf<PastEvent>()

        withContext(Dispatchers.IO) {
            // Use async to fetch past events
            val job = async {
                PastEventRepo.getPastEventsList(url)
            }

            // Await the result inside the IO coroutine
            pastEvents = job.await()
        }

        // Now, pastEvents is populated
        Log.d("Testlog", "newgetPastEvents: ${pastEvents}")

        // Update UI components after obtaining pastEvents
        withContext(Dispatchers.Main) {
            pastEventsRecyclerView.adapter = PastEventsAdapter(pastEvents)

            pastShimmer.stopShimmer()
            pastShimmer.visibility = View.GONE
            pastEventsRecyclerView.visibility = View.VISIBLE
        }
    }


    private suspend fun newgetUpcomingEvents() {

        var upcomingEvents = mutableListOf<UpcomingEvent>()

        withContext(Dispatchers.IO) {
            // Use async to fetch past events
            val job = async {
                UpcomingEventRepo.getUpcomingEventsList(url)
            }

            // Await the result inside the IO coroutine
            upcomingEvents = job.await()
        }

        // Now, pastEvents is populated
        Log.d("Testlog", "newgetUpcomingEvents: ${upcomingEvents}")

        // Update UI components after obtaining pastEvents
        withContext(Dispatchers.Main) {
            upcomingShimmer.stopShimmer()
            upcomingShimmer.visibility = View.GONE

            if(upcomingEvents.isEmpty()) {
                upcomingEventsRecyclerView.visibility = View.GONE
                upcomingPlaceHolder.visibility = View.VISIBLE
            } else {
                upcomingEventsRecyclerView.adapter = UpcomingEventsAdapter(upcomingEvents)
                upcomingEventsRecyclerView.visibility = View.VISIBLE
            }
        }
    }

}