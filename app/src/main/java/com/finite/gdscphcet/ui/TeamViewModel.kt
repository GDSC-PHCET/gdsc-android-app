package com.finite.gdscphcet.ui

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finite.gdscphcet.databinding.FragmentTeamBinding
import com.finite.gdscphcet.model.TeamMember
import com.finite.gdscphcet.repository.TeamRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {

    private val seniorTeamLiveData = MutableLiveData<List<TeamMember>>()
    val seniorTeam: LiveData<List<TeamMember>> = seniorTeamLiveData

    private val juniorTeamLiveData = MutableLiveData<List<TeamMember>>()
    val juniorTeam: LiveData<List<TeamMember>> = juniorTeamLiveData

    private val leadLiveData = MutableLiveData<TeamMember>()
    val leadData: LiveData<TeamMember> = leadLiveData


    fun getLeadDetails(binding: FragmentTeamBinding) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lead = TeamRepo.getLeadDetails()
                leadLiveData.postValue(lead)
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun getSeniorCoreTeamDetails(binding: FragmentTeamBinding) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val seniorMembers = TeamRepo.getSeniorCoreTeamDetails()
                seniorTeamLiveData.postValue(seniorMembers)
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun getJuniorCoreTeamDetails(binding: FragmentTeamBinding) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val juniorMembers = TeamRepo.getJuniorCoreTeamDetails()
                juniorTeamLiveData.postValue(juniorMembers)
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}