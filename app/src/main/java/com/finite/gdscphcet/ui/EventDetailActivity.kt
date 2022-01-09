package com.finite.gdscphcet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.navArgs
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.ActivityEventDetailBinding
import com.finite.gdscphcet.databinding.ActivityMainBinding
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.finite.gdscphcet.ui.viewModel.AboutViewModel
import com.finite.gdscphcet.ui.viewModel.EventDetailViewModel

class EventDetailActivity : AppCompatActivity() {

    val args: EventDetailActivityArgs by navArgs()
    private val viewModel: EventDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding?.apply {
            eventActivity = this@EventDetailActivity
            eventViewModel = viewModel
        }


        val toasttest = args.eventid
        Toast.makeText(this,toasttest,Toast.LENGTH_SHORT).show()
    }

    fun callFinish() {
        finish()
    }
}