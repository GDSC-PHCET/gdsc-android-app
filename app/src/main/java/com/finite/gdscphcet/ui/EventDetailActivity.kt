package com.finite.gdscphcet.ui

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.ActivityEventDetailBinding
import com.finite.gdscphcet.repository.PastEventRepo
import com.finite.gdscphcet.repository.UpcomingEventRepo
import com.finite.scrapingpractise.model.PastEventDetails
import com.finite.scrapingpractise.model.UpcomingEventDetails
import com.nabilmh.lottieswiperefreshlayout.LottieSwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventDetailActivity : AppCompatActivity() {


    private val viewModel: EventDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.status_bar)

        viewModel.initialiseUI(binding, intent, window)
        viewModel.setupListeners(binding, window, resources, intent)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.shareButton.setOnClickListener {
            // TODO : Add actual share logic, create a shareable image and share it
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hey! Check out this event by GDSC PHCET : ${viewModel.url}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }





    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}