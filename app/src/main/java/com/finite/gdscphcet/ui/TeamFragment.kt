package com.finite.gdscphcet.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.adapters.TeamAdapter
import com.finite.gdscphcet.databinding.FragmentTeamBinding

class TeamFragment : Fragment() {

    private var binding : FragmentTeamBinding? = null
    private lateinit var seniorAdapter: TeamAdapter
    private lateinit var juniorAdapter: TeamAdapter
    private lateinit var seniorRecyclerView: RecyclerView
    private lateinit var juniorRecyclerView: RecyclerView
    private lateinit var viewModel: TeamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[TeamViewModel::class.java]

        val fragmentBinding = FragmentTeamBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewSetup()
        setObservers()
        getTeamDetails()
        viewModel.setLeadCardUi(binding!!)

        binding!!.teamSwipeRefreshLayout.setOnRefreshListener {
            viewModel.changeStartColor()
            viewModel.setLeadCardUi(binding!!)
            getTeamDetails()
        }
    }



    private fun getTeamDetails() {
        viewModel.getSeniorCoreTeamDetails(binding!!)
        viewModel.getJuniorCoreTeamDetails(binding!!)
        viewModel.getLeadDetails(binding!!)
    }

    private fun initRecyclerViewSetup() {
        seniorRecyclerView = binding!!.seniorCoreTeamRecyclerView
        juniorRecyclerView = binding!!.juniorCoreTeamRecyclerView

        seniorAdapter = TeamAdapter(emptyList(), viewModel)
        juniorAdapter = TeamAdapter(emptyList(), viewModel)

        seniorRecyclerView.adapter = seniorAdapter
        juniorRecyclerView.adapter = juniorAdapter
    }

    private fun setObservers() {
        viewModel.seniorTeam.observe(viewLifecycleOwner) {
            seniorAdapter = TeamAdapter(it, viewModel)
            seniorRecyclerView.adapter = seniorAdapter
            seniorRecyclerView.setHasFixedSize(true)
            seniorAdapter.notifyDataSetChanged()
            if (binding!!.teamSwipeRefreshLayout.isRefreshing) binding!!.teamSwipeRefreshLayout.isRefreshing = false
        }

        viewModel.juniorTeam.observe(viewLifecycleOwner) {
            juniorAdapter = TeamAdapter(it, viewModel)
            juniorRecyclerView.adapter = juniorAdapter
            juniorRecyclerView.setHasFixedSize(true)
            juniorAdapter.notifyDataSetChanged()
            if (binding!!.teamSwipeRefreshLayout.isRefreshing) binding!!.teamSwipeRefreshLayout.isRefreshing = false
        }

        viewModel.leadData.observe(viewLifecycleOwner) {

            val lead = it

            binding!!.leadName.text = it.name
            binding!!.leadPosition.text = it.position

            Glide.with(binding!!.root.context.applicationContext)
                .load(it.image)
                .placeholder(R.drawable.ic_google_developers)
                .into(binding!!.leadImage)

            binding!!.leadLinkedIn.setOnClickListener {
                openLink(lead.linkedin!!)
            }

            binding!!.leadInstagram.setOnClickListener {
                openLink(lead.instagram!!)
            }

            binding!!.leadGithub.setOnClickListener {
                openLink(lead.github!!)
            }

            binding!!.leadTwitter.setOnClickListener {
                openLink(lead.twitter!!)
            }

            binding!!.leadGoogleDev.setOnClickListener {
                openLink(lead.googledev!!)
            }

            if (binding!!.teamSwipeRefreshLayout.isRefreshing) binding!!.teamSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}