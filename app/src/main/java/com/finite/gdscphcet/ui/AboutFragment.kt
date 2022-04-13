package com.finite.gdscphcet.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.FragmentAboutBinding
import com.finite.gdscphcet.ui.viewModel.AboutViewModel

class AboutFragment : Fragment() {

    private var binding : com.finite.gdscphcet.databinding.FragmentAboutBinding? = null
    private val viewModel: AboutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = FragmentAboutBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            aboutFragment = this@AboutFragment
            aboutViewModel = viewModel
        }
        binding?.androidDev?.setOnClickListener {
            onItemClick(R.string.android_dev)
        }
        binding?.flutterDev?.setOnClickListener {
            onItemClick(R.string.flutter_dev)
        }
        binding?.machinelearning?.setOnClickListener {
            onItemClick(R.string.machine_learning)
        }
        binding?.webdev?.setOnClickListener {
            onItemClick(R.string.web_dev)
        }
        binding?.googleCloud?.setOnClickListener {
            onItemClick(R.string.cloud_res)
        }
        binding?.openSource?.setOnClickListener {
            onItemClick(R.string.open_source)
        }

        binding?.arVr?.setOnClickListener {
            onItemClick(R.string.ar_vr_res)
        }
        binding?.graphicDesign?.setOnClickListener {
            onItemClick(R.string.graphic_design)
        }
        binding?.muchMore?.setOnClickListener {
            onItemClick(R.string.much_more)
        }
        binding?.learnMoreButton?.setOnClickListener {
            val url = "https://developers.google.com/community/gdsc"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    fun onItemClick(position: Int){
        Toast.makeText(context, "Coming soon! ^^", Toast.LENGTH_SHORT).show()
        val action = AboutFragmentDirections.actionAboutFragmentToTrackDetailActivity(position)
        findNavController().navigate(action)
    }

}