package com.finite.gdscphcet.ui

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finite.gdscphcet.databinding.FragmentTeamBinding
import com.finite.gdscphcet.model.TeamMember
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class TeamViewModel : ViewModel() {

    private val seniorTeamLiveData = MutableLiveData<List<TeamMember>>()
    val seniorTeam: LiveData<List<TeamMember>> = seniorTeamLiveData

    private val juniorTeamLiveData = MutableLiveData<List<TeamMember>>()
    val juniorTeam: LiveData<List<TeamMember>> = juniorTeamLiveData

    private val leadLiveData = MutableLiveData<TeamMember>()
    val leadData: LiveData<TeamMember> = leadLiveData

    fun getLeadDetails(dbRef: DatabaseReference, binding: FragmentTeamBinding) {
        dbRef.child("lead").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lead = snapshot.getValue(TeamMember::class.java)!!

                leadLiveData.value = lead

                if (binding.teamSwipeRefreshLayout.isRefreshing) binding.teamSwipeRefreshLayout.isRefreshing = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(binding.root.context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getSeniorCoreTeamDetails(dbRef: DatabaseReference, binding: FragmentTeamBinding) {
        dbRef.child("senior").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val seniorMembers = mutableListOf<TeamMember>()

                snapshot.children.forEach {
                    seniorMembers.add(it.getValue(TeamMember::class.java)!!)
                }
                seniorTeamLiveData.value = seniorMembers

                if (binding.teamSwipeRefreshLayout.isRefreshing) binding.teamSwipeRefreshLayout.isRefreshing = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(binding.root.context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getJuniorCoreTeamDetails(dbRef: DatabaseReference, binding: FragmentTeamBinding) {
        dbRef.child("junior").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val juniorMembers = mutableListOf<TeamMember>()

                snapshot.children.forEach {
                    juniorMembers.add(it.getValue(TeamMember::class.java)!!)
                }

                juniorTeamLiveData.value = juniorMembers

                if (binding.teamSwipeRefreshLayout.isRefreshing) binding.teamSwipeRefreshLayout.isRefreshing = false
            }

            override fun onCancelled(error: DatabaseError) {
                // Toast
                Toast.makeText(binding.root.context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

}