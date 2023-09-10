package com.finite.gdscphcet.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finite.gdscphcet.adapters.PastEventsAdapter
import com.finite.gdscphcet.adapters.UpcomingEventsAdapter
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.finite.gdscphcet.repository.PastEventRepo
import com.finite.gdscphcet.repository.UpcomingEventRepo
import com.finite.scrapingpractise.model.PastEvent
import com.finite.scrapingpractise.model.UpcomingEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // TODO : Fix App crash on internet connection off

    private val _pastEvents = MutableLiveData<List<PastEvent>?>(listOf())
    val pastEvents: LiveData<List<PastEvent>?> = _pastEvents

    private val _upcomingEvents = MutableLiveData<List<UpcomingEvent>?>()
    val upcomingEvents: LiveData<List<UpcomingEvent>?> = _upcomingEvents

    private val url = "https://gdsc.community.dev/pillai-hoc-college-of-engineering-and-technology-navi-mumbai/"
//    private val url = "https://gdsc.community.dev/dy-patil-college-of-engineering-pune/"
//    private val url = "https://gdsc.community.dev/mody-university-of-science-and-technology-laxmangarh/"
//    private val url = "https://gdsc.community.dev/sir-syed-university-of-engineering-and-technology/"


    private fun clearEvents() {
        _pastEvents.value = null
        _upcomingEvents.value = null
    }

    // observe the past events and update the UI
    private fun observePastEvents(binding: FragmentHomeBinding) {
        pastEvents.observeForever {

            if (it != null) {
                viewModelScope.launch {
                    binding.rvPastEvents.adapter = PastEventsAdapter(_pastEvents.value!!)
                    binding.shimmerPastEvent.visibility = android.view.View.GONE
                    binding.shimmerPastEvent.stopShimmer()
                    binding.rvPastEvents.visibility = android.view.View.VISIBLE
                    if (binding.homeSwipeRefreshLayout.isRefreshing) binding.homeSwipeRefreshLayout.isRefreshing =
                        false
                }
            }
        }
    }

    private fun observeUpcomingEvents(binding: FragmentHomeBinding) {
        upcomingEvents.observeForever {
            if (it != null) {
                if (_upcomingEvents.value!!.isEmpty()) {
                    binding.upcomingEventsPlaceholderLayout.visibility = android.view.View.VISIBLE
                    binding.shimmerUpcomingEvent.visibility = android.view.View.GONE
                    binding.shimmerUpcomingEvent.stopShimmer()
                    binding.rvUpcomingEvents.visibility = android.view.View.GONE
                } else {
                    viewModelScope.launch {
                        binding.rvUpcomingEvents.adapter =
                            UpcomingEventsAdapter(_upcomingEvents.value!!)
                        binding.shimmerUpcomingEvent.visibility = android.view.View.GONE
                        binding.upcomingEventsPlaceholderLayout.visibility = android.view.View.GONE
                        binding.shimmerUpcomingEvent.stopShimmer()
                        binding.rvUpcomingEvents.visibility = android.view.View.VISIBLE
                    }
                }

                if (binding.homeSwipeRefreshLayout.isRefreshing) binding.homeSwipeRefreshLayout.isRefreshing =
                    false
            }
        }
    }

    fun loadPastEvents(binding: FragmentHomeBinding, update: Boolean = false) {

        binding.shimmerPastEvent.startShimmer()
        binding.shimmerPastEvent.visibility = android.view.View.VISIBLE
        binding.rvPastEvents.visibility = android.view.View.GONE

        if (update) {
            viewModelScope.launch {
                clearEvents()
                val job = async(Dispatchers.IO) {
                    PastEventRepo.getPastEventsList(url)
                }
                val pastEvents = job.await()
                _pastEvents.postValue(pastEvents)
                observePastEvents(binding)
            }
        } else {
            viewModelScope.launch {
                delay(500)
                observePastEvents(binding)
            }

        }
    }

    fun loadUpcomingEvents(binding: FragmentHomeBinding, update: Boolean = false) {

        binding.shimmerUpcomingEvent.startShimmer()
        binding.shimmerUpcomingEvent.visibility = android.view.View.VISIBLE
        binding.rvUpcomingEvents.visibility = android.view.View.GONE
        binding.upcomingEventsPlaceholderLayout.visibility = android.view.View.GONE

        if (update) {
            viewModelScope.launch {
                clearEvents()
                val job = async(Dispatchers.IO) {
                    UpcomingEventRepo.getUpcomingEventsList(url)
                }
                val upcomingEvents = job.await()
                _upcomingEvents.postValue(upcomingEvents)
                observeUpcomingEvents(binding)
            }
        } else {
            viewModelScope.launch {
                delay(500)
                observeUpcomingEvents(binding)
            }
        }
    }
}