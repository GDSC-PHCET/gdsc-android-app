package com.finite.gdscphcet.ui

import android.app.PendingIntent.OnFinished
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.adapters.PastEventsAdapter
import com.finite.gdscphcet.databinding.ActivityEventDetailBinding
import com.finite.gdscphcet.repository.PastEventRepo
import com.finite.gdscphcet.repository.UpcomingEventRepo
import com.finite.scrapingpractise.model.PastEventDetails
import com.finite.scrapingpractise.model.UpcomingEventDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventDetailActivity : AppCompatActivity() {

    private var color = ""
    private var url = ""
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val window = this.window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.statusBarColor = this.resources.getColor(R.color.status_bar)

        window.statusBarColor = resources.getColor(R.color.status_bar)

        initialiseUI(binding)

        swipeRefreshLayout = binding.eventDetailSwipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            window.statusBarColor = resources.getColor(R.color.status_bar)
            initialiseUI(binding)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.shareButton.setOnClickListener {
            // TODO : Add actual share logic, create a shareable image and share it
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hey! Check out this event by GDSC PHCET : $url")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun initialiseUI(binding: ActivityEventDetailBinding) {
        binding.uiConstraintLayout.visibility = View.GONE
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()

        val eventType = intent.getStringExtra("eventType")
        url = intent.getStringExtra("eventUrl") ?: ""
        color = intent.getStringExtra("color") ?: ""

        Log.d("Testlog", "onCreate: $eventType : $url")


        CoroutineScope(Dispatchers.Main).launch {
            when (eventType) {
                "upcoming" -> loadUpcomingEventDetails(url, binding)
                "past" -> loadPastEventDetails(url, binding)
            }
        }
    }

    private suspend fun loadUpcomingEventDetails(url: String, binding: ActivityEventDetailBinding) {
        Log.d("Testlog", "Reached loadUpcomingEventDetails:")
        var upcomingEvents = UpcomingEventDetails()
        withContext(Dispatchers.IO) {

            val job = async {
                UpcomingEventRepo.getUpcomingEventDetails(url)
            }
            upcomingEvents = job.await()
        }

        withContext(Dispatchers.Main) {
            binding.apply {
                Glide.with(this@EventDetailActivity).load(upcomingEvents.logoUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.logoImageView)
                Glide.with(this@EventDetailActivity).load(upcomingEvents.bannerUrl)
                    .placeholder(R.drawable.gdsc_banner)
                    .into(binding.coverBannerImageView)
                setupUpcomingUI(upcomingEvents, binding)

                stopShimmer(binding)
                if(swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun stopShimmer(binding: ActivityEventDetailBinding) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.uiConstraintLayout.visibility = View.VISIBLE
    }

    private suspend fun loadPastEventDetails(url: String, binding: ActivityEventDetailBinding) {
        //Log.d("Testlog", "Reached loadPastEventDetails: $url")
        var pastEvents = PastEventDetails()
        withContext(Dispatchers.IO) {
            val job = async {
                PastEventRepo.getPastEventDetails(url)
            }
            pastEvents = job.await()
            //Log.d("Testlog", "loadPastEventDetails: $pastEvents")
        }

        withContext(Dispatchers.Main) {
            binding.apply {
                Glide.with(this@EventDetailActivity).load(pastEvents.logoUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.logoImageView)
                Glide.with(this@EventDetailActivity).load(pastEvents.bannerUrl)
                    .placeholder(R.drawable.gdsc_banner)
                    .into(binding.coverBannerImageView)
                setupPastUI(pastEvents, binding)

                stopShimmer(binding)
                if(swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupUpcomingUI(
        upcomingEvents: UpcomingEventDetails,
        binding: ActivityEventDetailBinding
    ) {
        binding.apply {
            binding.eventTitleTextView.text = upcomingEvents.title

            if(upcomingEvents.whenDate.isEmpty() || upcomingEvents.whenTime.isEmpty()){
                val parts = upcomingEvents.dateTime.split(",").map { it.trim() }
                binding.eventDateTextView.text = "${parts[0]}, ${parts[1]}" // Date is composed of parts[0] and parts[1]
                binding.eventTimeTextView.text =  parts[2]
            } else {
                binding.eventDateTextView.text = upcomingEvents.whenDate
                binding.eventTimeTextView.text = upcomingEvents.whenTime
            }


            binding.eventModeTextView.text = upcomingEvents.mode
            binding.longDescriptionWebView.loadData(
                upcomingEvents.longDesc,
                "text/html",
                "UTF-8"
            )

            binding.tagsContainer.removeAllViews()

            for (tag in upcomingEvents.tags) {
                val textView = TextView(this@EventDetailActivity)
                textView.text = tag
                textView.textSize = 10f
                textView.setTextColor(
                    ContextCompat.getColor(
                        this@EventDetailActivity,
                        R.color.black
                    )
                )

                val layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                layoutParams.setMargins(
                    0,
                    0,
                    resources.getDimensionPixelSize(R.dimen.tag_margin_end),
                    resources.getDimensionPixelSize(R.dimen.tag_margin_bottom)
                )
                textView.layoutParams = layoutParams
                textView.setPadding(
                    resources.getDimensionPixelSize(R.dimen.tag_padding_start),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_top),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_end),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_bottom)
                )

                when (color) {
                    "blue" -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(
                            ContextCompat.getColor(
                                this@EventDetailActivity,
                                R.color.google_blue_alpha_45
                            )
                        )
                        textView.background = background
                    }

                    "red" -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(
                            ContextCompat.getColor(
                                this@EventDetailActivity,
                                R.color.google_red_alpha_45
                            )
                        )
                        textView.background = background
                    }

                    "yellow" -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(
                            ContextCompat.getColor(
                                this@EventDetailActivity,
                                R.color.google_yellow_alpha_45
                            )
                        )
                        textView.background = background
                    }

                    "green" -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(
                            ContextCompat.getColor(
                                this@EventDetailActivity,
                                R.color.google_green_alpha_45
                            )
                        )
                        textView.background = background
                    }
                }

                binding.tagsContainer.addView(textView)
            }

            when (color) {
                "blue" -> {
                    detailsCardView.setCardBackgroundColor(resources.getColor(R.color.google_blue_alpha_15))
                    eventDateTextView.setTextColor(resources.getColor(R.color.google_blue))
                    eventTimeTextView.setTextColor(resources.getColor(R.color.google_blue))
                    eventModeTextView.setTextColor(resources.getColor(R.color.google_blue))
                    eventDateImageView.setColorFilter(resources.getColor(R.color.google_blue))
                    eventTimeImageView.setColorFilter(resources.getColor(R.color.google_blue))
                    eventModeImageView.setColorFilter(resources.getColor(R.color.google_blue))
                    eventTagsImageView.setColorFilter(resources.getColor(R.color.google_blue))
                    longDescriptionWebView.setBackgroundColor(resources.getColor(R.color.google_blue_alpha_5))
                    nestedScrollView.setBackgroundColor(resources.getColor(R.color.google_blue_alpha_5))
                    logoImageView.borderColor = resources.getColor(R.color.google_blue_45_opaque)
                    window.statusBarColor = resources.getColor(R.color.google_blue_alpha_45)
                    //backButton.backgroundTintList = AppCompatResources.getColorStateList(this@EventDetailActivity, R.color.google_blue)
                }

                "red" -> {
                    detailsCardView.setCardBackgroundColor(resources.getColor(R.color.google_red_alpha_15))
                    eventDateTextView.setTextColor(resources.getColor(R.color.google_red))
                    eventTimeTextView.setTextColor(resources.getColor(R.color.google_red))
                    eventModeTextView.setTextColor(resources.getColor(R.color.google_red))
                    eventDateImageView.setColorFilter(resources.getColor(R.color.google_red))
                    eventTimeImageView.setColorFilter(resources.getColor(R.color.google_red))
                    eventModeImageView.setColorFilter(resources.getColor(R.color.google_red))
                    eventTagsImageView.setColorFilter(resources.getColor(R.color.google_red))
                    longDescriptionWebView.setBackgroundColor(resources.getColor(R.color.google_red_alpha_5))
                    nestedScrollView.setBackgroundColor(resources.getColor(R.color.google_red_alpha_5))
                    logoImageView.borderColor = resources.getColor(R.color.google_red_45_opaque)
                    window.statusBarColor = resources.getColor(R.color.google_red_alpha_45)
                    //backButton.backgroundTintList = AppCompatResources.getColorStateList(this@EventDetailActivity, R.color.google_red)
                }

                "yellow" -> {
                    detailsCardView.setCardBackgroundColor(resources.getColor(R.color.google_yellow_alpha_15))
                    eventDateTextView.setTextColor(resources.getColor(R.color.google_yellow))
                    eventTimeTextView.setTextColor(resources.getColor(R.color.google_yellow))
                    eventModeTextView.setTextColor(resources.getColor(R.color.google_yellow))
                    eventDateImageView.setColorFilter(resources.getColor(R.color.google_yellow))
                    eventTimeImageView.setColorFilter(resources.getColor(R.color.google_yellow))
                    eventModeImageView.setColorFilter(resources.getColor(R.color.google_yellow))
                    eventTagsImageView.setColorFilter(resources.getColor(R.color.google_yellow))
                    longDescriptionWebView.setBackgroundColor(resources.getColor(R.color.google_yellow_alpha_5))
                    nestedScrollView.setBackgroundColor(resources.getColor(R.color.google_yellow_alpha_5))
                    logoImageView.borderColor = resources.getColor(R.color.google_yellow_45_opaque)
                    window.statusBarColor = resources.getColor(R.color.google_yellow_alpha_45)
                    //backButton.backgroundTintList = AppCompatResources.getColorStateList(this@EventDetailActivity, R.color.google_yellow)
                }

                "green" -> {
                    detailsCardView.setCardBackgroundColor(resources.getColor(R.color.google_green_alpha_15))
                    eventDateTextView.setTextColor(resources.getColor(R.color.google_green))
                    eventTimeTextView.setTextColor(resources.getColor(R.color.google_green))
                    eventModeTextView.setTextColor(resources.getColor(R.color.google_green))
                    eventDateImageView.setColorFilter(resources.getColor(R.color.google_green))
                    eventTimeImageView.setColorFilter(resources.getColor(R.color.google_green))
                    eventModeImageView.setColorFilter(resources.getColor(R.color.google_green))
                    eventTagsImageView.setColorFilter(resources.getColor(R.color.google_green))
                    longDescriptionWebView.setBackgroundColor(resources.getColor(R.color.google_green_alpha_5))
                    nestedScrollView.setBackgroundColor(resources.getColor(R.color.google_green_alpha_5))
                    logoImageView.borderColor = resources.getColor(R.color.google_green_45_opaque)
                    window.statusBarColor = resources.getColor(R.color.google_green_alpha_45)
                    //backButton.backgroundTintList = AppCompatResources.getColorStateList(this@EventDetailActivity, R.color.google_green)
                }
            }
        }
    }

    private fun setupPastUI(
        pastEvents: PastEventDetails,
        binding: ActivityEventDetailBinding
    ) {
        binding.apply {
            binding.eventTitleTextView.text = pastEvents.title

            if(pastEvents.whenDate.isEmpty() || pastEvents.whenTime.isEmpty()){
                val parts = pastEvents.dateTime.split(",").map { it.trim() }
                binding.eventDateTextView.text = "${parts[0]}, ${parts[1]}" // Date is composed of parts[0] and parts[1]
                binding.eventTimeTextView.text =  parts[2]
            } else {
                binding.eventDateTextView.text = pastEvents.whenDate
                binding.eventTimeTextView.text = pastEvents.whenTime
            }

            binding.eventModeTextView.text = pastEvents.mode
            //Log.d("Testlog", "setupPastUI: ${pastEvents.longDesc}")
            binding.longDescriptionWebView.loadData(
                pastEvents.longDesc,
                "text/html",
                "UTF-8"
            )

            binding.tagsContainer.removeAllViews()

            for (tag in pastEvents.tags) {
                val textView = TextView(this@EventDetailActivity)
                textView.text = tag
                textView.textSize = 10f
                textView.setTextColor(
                    ContextCompat.getColor(
                        this@EventDetailActivity,
                        R.color.black
                    )
                )

                val layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                layoutParams.setMargins(
                    0,
                    0,
                    resources.getDimensionPixelSize(R.dimen.tag_margin_end),
                    resources.getDimensionPixelSize(R.dimen.tag_margin_bottom)
                )
                textView.layoutParams = layoutParams
                textView.setPadding(
                    resources.getDimensionPixelSize(R.dimen.tag_padding_start),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_top),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_end),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_bottom)
                )

                when (color) {
                    "blue" -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(
                            ContextCompat.getColor(
                                this@EventDetailActivity,
                                R.color.google_blue_alpha_45
                            )
                        )
                        textView.background = background
                    }

                    "red" -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(
                            ContextCompat.getColor(
                                this@EventDetailActivity,
                                R.color.google_red_alpha_45
                            )
                        )
                        textView.background = background
                    }

                    "yellow" -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(
                            ContextCompat.getColor(
                                this@EventDetailActivity,
                                R.color.google_yellow_alpha_45
                            )
                        )
                        textView.background = background
                    }

                    "green" -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(
                            ContextCompat.getColor(
                                this@EventDetailActivity,
                                R.color.google_green_alpha_45
                            )
                        )
                        textView.background = background
                    }
                }

                binding.tagsContainer.addView(textView)
            }

            when (color) {
                "blue" -> {
                    detailsCardView.setCardBackgroundColor(resources.getColor(R.color.google_blue_alpha_15))
                    eventDateTextView.setTextColor(resources.getColor(R.color.google_blue))
                    eventTimeTextView.setTextColor(resources.getColor(R.color.google_blue))
                    eventModeTextView.setTextColor(resources.getColor(R.color.google_blue))
                    eventDateImageView.setColorFilter(resources.getColor(R.color.google_blue))
                    eventTimeImageView.setColorFilter(resources.getColor(R.color.google_blue))
                    eventModeImageView.setColorFilter(resources.getColor(R.color.google_blue))
                    eventTagsImageView.setColorFilter(resources.getColor(R.color.google_blue))
                    longDescriptionWebView.setBackgroundColor(resources.getColor(R.color.google_blue_alpha_5))
                    nestedScrollView.setBackgroundColor(resources.getColor(R.color.google_blue_alpha_5))
                    logoImageView.borderColor = resources.getColor(R.color.google_blue_45_opaque)
                    window.statusBarColor = resources.getColor(R.color.google_blue_alpha_45)
                    //backButton.backgroundTintList = AppCompatResources.getColorStateList(this@EventDetailActivity, R.color.google_blue)
                }

                "red" -> {
                    detailsCardView.setCardBackgroundColor(resources.getColor(R.color.google_red_alpha_15))
                    eventDateTextView.setTextColor(resources.getColor(R.color.google_red))
                    eventTimeTextView.setTextColor(resources.getColor(R.color.google_red))
                    eventModeTextView.setTextColor(resources.getColor(R.color.google_red))
                    eventDateImageView.setColorFilter(resources.getColor(R.color.google_red))
                    eventTimeImageView.setColorFilter(resources.getColor(R.color.google_red))
                    eventModeImageView.setColorFilter(resources.getColor(R.color.google_red))
                    eventTagsImageView.setColorFilter(resources.getColor(R.color.google_red))
                    longDescriptionWebView.setBackgroundColor(resources.getColor(R.color.google_red_alpha_5))
                    nestedScrollView.setBackgroundColor(resources.getColor(R.color.google_red_alpha_5))
                    logoImageView.borderColor = resources.getColor(R.color.google_red_45_opaque)
                    window.statusBarColor = resources.getColor(R.color.google_red_alpha_45)
                    //backButton.backgroundTintList = AppCompatResources.getColorStateList(this@EventDetailActivity, R.color.google_red)
                }

                "yellow" -> {
                    detailsCardView.setCardBackgroundColor(resources.getColor(R.color.google_yellow_alpha_15))
                    eventDateTextView.setTextColor(resources.getColor(R.color.google_yellow))
                    eventTimeTextView.setTextColor(resources.getColor(R.color.google_yellow))
                    eventModeTextView.setTextColor(resources.getColor(R.color.google_yellow))
                    eventDateImageView.setColorFilter(resources.getColor(R.color.google_yellow))
                    eventTimeImageView.setColorFilter(resources.getColor(R.color.google_yellow))
                    eventModeImageView.setColorFilter(resources.getColor(R.color.google_yellow))
                    eventTagsImageView.setColorFilter(resources.getColor(R.color.google_yellow))
                    longDescriptionWebView.setBackgroundColor(resources.getColor(R.color.google_yellow_alpha_5))
                    nestedScrollView.setBackgroundColor(resources.getColor(R.color.google_yellow_alpha_5))
                    logoImageView.borderColor = resources.getColor(R.color.google_yellow_45_opaque)
                    window.statusBarColor = resources.getColor(R.color.google_yellow_alpha_45)
                    //backButton.backgroundTintList = AppCompatResources.getColorStateList(this@EventDetailActivity, R.color.google_yellow)
                }

                "green" -> {
                    detailsCardView.setCardBackgroundColor(resources.getColor(R.color.google_green_alpha_15))
                    eventDateTextView.setTextColor(resources.getColor(R.color.google_green))
                    eventTimeTextView.setTextColor(resources.getColor(R.color.google_green))
                    eventModeTextView.setTextColor(resources.getColor(R.color.google_green))
                    eventDateImageView.setColorFilter(resources.getColor(R.color.google_green))
                    eventTimeImageView.setColorFilter(resources.getColor(R.color.google_green))
                    eventModeImageView.setColorFilter(resources.getColor(R.color.google_green))
                    eventTagsImageView.setColorFilter(resources.getColor(R.color.google_green))
                    longDescriptionWebView.setBackgroundColor(resources.getColor(R.color.google_green_alpha_5))
                    nestedScrollView.setBackgroundColor(resources.getColor(R.color.google_green_alpha_5))
                    logoImageView.borderColor = resources.getColor(R.color.google_green_45_opaque)
                    window.statusBarColor = resources.getColor(R.color.google_green_alpha_45)
                    //backButton.backgroundTintList = AppCompatResources.getColorStateList(this@EventDetailActivity, R.color.google_green)
                }
            }
        }
    }

    // when back button is pressed finish the activity
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}