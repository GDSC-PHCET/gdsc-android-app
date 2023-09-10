package com.finite.gdscphcet.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.ActivityEventDetailBinding
import com.finite.gdscphcet.repository.PastEventRepo
import com.finite.gdscphcet.repository.UpcomingEventRepo
import com.finite.gdscphcet.utils.ExtractTextFromHTML
import com.finite.scrapingpractise.model.PastEventDetails
import com.finite.scrapingpractise.model.UpcomingEventDetails
import com.nabilmh.lottieswiperefreshlayout.LottieSwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class EventDetailViewModel : ViewModel() {

    var url = ""
    var longDesc = ""
    var color = ""
    private lateinit var swipeRefreshLayout: LottieSwipeRefreshLayout

    fun initialiseUI(binding: ActivityEventDetailBinding, intent: Intent, window: Window) {
        binding.uiConstraintLayout.visibility = View.GONE
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()

        val eventType = intent.getStringExtra("eventType")
        url = intent.getStringExtra("eventUrl") ?: ""
        color = intent.getStringExtra("color") ?: ""

        CoroutineScope(Dispatchers.Main).launch {
            when (eventType) {
                "upcoming" -> loadUpcomingEventDetails(url, binding, context = binding.root.context, resources = binding.root.resources, window = window)
                "past" -> loadPastEventDetails(url, binding, context = binding.root.context, resources = binding.root.resources, window = window)
            }
        }
    }


    private suspend fun loadUpcomingEventDetails(url: String, binding: ActivityEventDetailBinding, context: Context, resources: Resources, window: Window) {
        var upcomingEvents = UpcomingEventDetails()
        withContext(Dispatchers.IO) {
            val job = async {
                UpcomingEventRepo.getUpcomingEventDetails(url)
            }
            upcomingEvents = job.await()
        }

        longDesc = upcomingEvents.longDesc

        withContext(Dispatchers.Main) {
            setupBasicDetails(
                logoUrl = upcomingEvents.logoUrl,
                bannerUrl = upcomingEvents.bannerUrl,
                title = upcomingEvents.title,
                whenDate = upcomingEvents.whenDate,
                whenTime = upcomingEvents.whenTime,
                mode = upcomingEvents.mode,
                longDesc = upcomingEvents.longDesc,
                dateTime = upcomingEvents.dateTime,
                binding = binding,
                context = context
            )

            setupEventTags(binding, upcomingEvents.tags, context, resources)
            setupColors(binding, "upcoming", resources = resources, window = window)

            stopShimmer(binding)
            if(swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
        }
    }

    private suspend fun loadPastEventDetails(url: String, binding: ActivityEventDetailBinding, context: Context, resources: Resources, window: Window) {
        var pastEvents = PastEventDetails()
        withContext(Dispatchers.IO) {
            val job = async {
                PastEventRepo.getPastEventDetails(url)
            }
            pastEvents = job.await()
        }

        longDesc = pastEvents.longDesc

        withContext(Dispatchers.Main) {
            setupBasicDetails(
                logoUrl = pastEvents.logoUrl,
                bannerUrl = pastEvents.bannerUrl,
                title = pastEvents.title,
                whenDate = pastEvents.whenDate,
                whenTime = pastEvents.whenTime,
                mode = pastEvents.mode,
                longDesc = pastEvents.longDesc,
                dateTime = pastEvents.dateTime,
                binding = binding,
                context = context
            )

            setupEventTags(binding, pastEvents.tags, context = context, resources = resources)
            setupColors(binding, "past", resources = resources, window = window)

            stopShimmer(binding)
            if(swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupBasicDetails(
        logoUrl : String = "",
        bannerUrl : String = "",
        title : String = "",
        whenDate : String = "",
        whenTime : String = "",
        mode : String = "",
        longDesc : String = "",
        dateTime : String = "",
        binding: ActivityEventDetailBinding,
        context: Context
    ) {

        binding.apply {
            Glide.with(context).load(logoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(logoImageView)
            Glide.with(context).load(bannerUrl)
                .placeholder(R.drawable.gdsc_banner)
                .into(coverBannerImageView)

            eventTitleTextView.text = title

            if(whenDate.isEmpty() || whenTime.isEmpty()){
                val parts = dateTime.split(",").map { it.trim() }
                eventDateTextView.text = "${parts[0]}, ${parts[1]}" // Date is composed of parts[0] and parts[1]
                eventTimeTextView.text =  parts[2]
            } else {
                eventDateTextView.text = whenDate
                eventTimeTextView.text = whenTime
            }

            eventModeTextView.text = mode
            longDescriptionWebView.loadData(
                longDesc,
                "text/html",
                "UTF-8"
            )
        }
    }

    private fun setupColors(binding: ActivityEventDetailBinding, eventType: String, resources: Resources, window: Window) {
        binding.apply {
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
                }
            }
        }
    }

    private fun setupEventTags(binding: ActivityEventDetailBinding, tags : List<String>, context: Context, resources: Resources) {
        binding.tagsContainer.removeAllViews()
        for (tag in tags) {
            val textView = TextView(context)
            textView.text = tag
            textView.textSize = 10f
            textView.setTextColor(
                ContextCompat.getColor(
                    context,
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

            val background = GradientDrawable()
            background.cornerRadius = resources.getDimension(R.dimen.corner_radius)

            val color = when(color) {
                "blue" -> R.color.google_blue_alpha_45
                "red" -> R.color.google_red_alpha_45
                "yellow" -> R.color.google_yellow_alpha_45
                "green" -> R.color.google_green_alpha_45
                else -> R.color.google_blue_alpha_45
            }

            background.setColor(ContextCompat.getColor(context, color))
            textView.background = background
            binding.tagsContainer.addView(textView)
        }
    }

    private fun stopShimmer(binding: ActivityEventDetailBinding) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.uiConstraintLayout.visibility = View.VISIBLE
    }

    fun setupListeners(binding: ActivityEventDetailBinding, window: Window, intent: Intent) {
        swipeRefreshLayout = binding.eventDetailSwipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            window.statusBarColor = binding.root.resources.getColor(R.color.status_bar)
            initialiseUI(binding, intent, window)
        }

        binding.shareButton.setOnClickListener {
            shareEvent(binding,binding.root.context as EventDetailActivity)
        }
    }

    private fun shareEvent(binding: ActivityEventDetailBinding, context: EventDetailActivity) {
        CoroutineScope(Dispatchers.Main).launch {
            val uiLayout = binding.uiConstraintLayout
            binding.shareButton.visibility = View.GONE
            binding.backButton.visibility = View.GONE
            binding.extraDetailsLinearLayout.visibility = View.GONE

            when(color) {
                "blue" -> uiLayout.background = ColorDrawable(ContextCompat.getColor(context, R.color.google_blue_alpha_5))
                "red" -> uiLayout.background = ColorDrawable(ContextCompat.getColor(context, R.color.google_red_alpha_5))
                "yellow" -> uiLayout.background = ColorDrawable(ContextCompat.getColor(context, R.color.google_yellow_alpha_5))
                "green" -> uiLayout.background = ColorDrawable(ContextCompat.getColor(context, R.color.google_green_alpha_5))
            }

            delay(100)

            val bitmap = Bitmap.createBitmap(uiLayout.width, uiLayout.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            uiLayout.draw(canvas)

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "${"Test-Share-Image"} ${System.currentTimeMillis()}", null)
            val uri: Uri = Uri.parse(path)

            uiLayout.background = ColorDrawable(Color.TRANSPARENT)

            binding.shareButton.visibility = View.VISIBLE
            binding.backButton.visibility = View.VISIBLE
            binding.extraDetailsLinearLayout.visibility = View.VISIBLE

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"

            Log.d("Testlog", "Original HTML: \n${longDesc}")

            intent.putExtra(Intent.EXTRA_TEXT, "${ExtractTextFromHTML.extract(longDesc)}\n\n${url}")
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(Intent.createChooser(intent, "Share"))
        }
    }
}