package com.finite.gdscphcet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finite.gdscphcet.R
import com.finite.gdscphcet.adapters.PastEventsAdapter
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.finite.gdscphcet.model.PastEventModel
import com.finite.gdscphcet.ui.viewModel.HomeViewModel
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private var binding : FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var pastEventsRecyclerView: RecyclerView
    private lateinit var pastEventsList: ArrayList<PastEventModel>

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

        binding?.verifyButton?.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCertificateActivity()
            findNavController().navigate(action)
        }

        pastEventsRecyclerView = binding!!.rvPastEvents
        pastEventsRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        pastEventsRecyclerView.setHasFixedSize(true)
        pastEventsList = arrayListOf()
        getPastEventsData()

    }

    private fun getPastEventsData() {
        dbref = FirebaseDatabase.getInstance().getReference("pastEvents")

        dbref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (eventSnapshot in snapshot.children){

                        val event = eventSnapshot.getValue(PastEventModel::class.java)
                        pastEventsList.add(event!!)

                    }
                    pastEventsRecyclerView.adapter = PastEventsAdapter(requireContext(),pastEventsList.reversed() as MutableList<PastEventModel>)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }


        })
    }


}