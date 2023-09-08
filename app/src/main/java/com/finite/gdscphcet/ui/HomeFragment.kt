package com.finite.gdscphcet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.finite.gdscphcet.adapters.PastEventsAdapter
import com.finite.gdscphcet.adapters.UpcomingEventsAdapter
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.finite.gdscphcet.repository.PastEventRepo
import com.finite.gdscphcet.repository.UpcomingEventRepo
import com.finite.scrapingpractise.model.PastEvent
import com.finite.scrapingpractise.model.UpcomingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private lateinit var pastEventsRecyclerView: RecyclerView
    private lateinit var upcomingEventsRecyclerView: RecyclerView

    private lateinit var upcomingPlaceHolder: LinearLayout
    private lateinit var upcomingShimmer: ShimmerFrameLayout
    private lateinit var pastShimmer: ShimmerFrameLayout

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

//        private val url = "https://gdsc.community.dev/pillai-hoc-college-of-engineering-and-technology-navi-mumbai/"
    private val url = "https://gdsc.community.dev/dy-patil-college-of-engineering-pune/"
//        private val url = "https://gdsc.community.dev/mody-university-of-science-and-technology-laxmangarh/"

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

        initialiseShimmer()
        initialiseRecyclerViews()
        updateEvents()


        swipeRefreshLayout = binding!!.homeSwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            updateEvents()
        }
    }

    private fun updateEvents() {
        upcomingEventsRecyclerView.visibility = View.GONE
        pastEventsRecyclerView.visibility = View.GONE
        upcomingPlaceHolder.visibility = View.GONE

        upcomingShimmer.visibility = View.VISIBLE
        pastShimmer.visibility = View.VISIBLE
        upcomingShimmer.startShimmer()
        pastShimmer.startShimmer()

        CoroutineScope(Dispatchers.Main).launch {
            getPastEvents()
        }

        CoroutineScope(Dispatchers.Main).launch {
            getUpcomingEvents()
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

    private fun initialiseShimmer() {
        upcomingShimmer = binding!!.shimmerUpcomingEvent
        pastShimmer = binding!!.shimmerPastEvent
        upcomingPlaceHolder = binding!!.upcomingEventsPlaceholderLayout

        upcomingShimmer.startShimmer()
        pastShimmer.startShimmer()
    }

    private suspend fun getPastEvents() {

        var pastEvents = mutableListOf<PastEvent>()

        withContext(Dispatchers.IO) {
            val job = async {
                PastEventRepo.getPastEventsList(url)
            }
            pastEvents = job.await()
        }

        withContext(Dispatchers.Main) {
            pastEventsRecyclerView.adapter = PastEventsAdapter(pastEvents)
            pastShimmer.stopShimmer()
            pastShimmer.visibility = View.GONE
            pastEventsRecyclerView.visibility = View.VISIBLE
            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
        }
    }

    private suspend fun getUpcomingEvents() {

        var upcomingEvents = mutableListOf<UpcomingEvent>()

        withContext(Dispatchers.IO) {
            val job = async {
                UpcomingEventRepo.getUpcomingEventsList(url)
            }
            upcomingEvents = job.await()
        }

        withContext(Dispatchers.Main) {
            upcomingShimmer.stopShimmer()
            upcomingShimmer.visibility = View.GONE
            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
            if (upcomingEvents.isEmpty()) {
                upcomingEventsRecyclerView.visibility = View.GONE
                upcomingPlaceHolder.visibility = View.VISIBLE
            } else {
                upcomingEventsRecyclerView.adapter = UpcomingEventsAdapter(upcomingEvents)
                upcomingEventsRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}