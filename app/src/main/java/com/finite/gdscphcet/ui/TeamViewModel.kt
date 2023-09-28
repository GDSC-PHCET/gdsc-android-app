package com.finite.gdscphcet.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.FragmentTeamBinding
import com.finite.gdscphcet.model.TeamMember
import com.finite.gdscphcet.repository.TeamRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {

    var startColorNumber = (1..4).random()

    private val seniorTeamLiveData = MutableLiveData<List<TeamMember>>()
    val seniorTeam: LiveData<List<TeamMember>> = seniorTeamLiveData

    private val juniorTeamLiveData = MutableLiveData<List<TeamMember>>()
    val juniorTeam: LiveData<List<TeamMember>> = juniorTeamLiveData

    private val leadLiveData = MutableLiveData<TeamMember>()
    val leadData: LiveData<TeamMember> = leadLiveData


    fun getLeadDetails(binding: FragmentTeamBinding, getNewData: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("TestLog", "getLeadDetails: Called")
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
                Log.d("TestLog", "getSeniorCoreTeamDetails: Called")
                val seniorMembers = TeamRepo.getSeniorCoreTeamDetails().shuffled()
                seniorTeamLiveData.postValue(seniorMembers)
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun getJuniorCoreTeamDetails(binding: FragmentTeamBinding) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("TestLog", "getJuniorCoreTeamDetails: Called")
                val juniorMembers = TeamRepo.getJuniorCoreTeamDetails().shuffled()
                juniorTeamLiveData.postValue(juniorMembers)
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setLeadCardUi(binding: FragmentTeamBinding) {

        when (startColorNumber) {
            1 -> {
                binding.leadName.setTextColor(binding.root.resources.getColor(R.color.google_green))
                binding.leadCardView.setCardBackgroundColor(binding.root.resources.getColor(R.color.google_green_alpha_15))
            }
            2 -> {
                binding.leadName.setTextColor(binding.root.resources.getColor(R.color.google_blue))
                binding.leadCardView.setCardBackgroundColor(binding.root.resources.getColor(R.color.google_blue_alpha_15))
            }
            3 -> {
                binding.leadName.setTextColor(binding.root.resources.getColor(R.color.google_red))
                binding.leadCardView.setCardBackgroundColor(binding.root.resources.getColor(R.color.google_red_alpha_15))
            }
            4 -> {
                binding.leadName.setTextColor(binding.root.resources.getColor(R.color.google_yellow))
                binding.leadCardView.setCardBackgroundColor(binding.root.resources.getColor(R.color.google_yellow_alpha_15))
            }
        }
    }

    fun changeStartColor() {
        startColorNumber = (1..4).random()
    }
}