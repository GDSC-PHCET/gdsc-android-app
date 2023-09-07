package com.finite.gdscphcet.adapters

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.ui.EventDetailActivity
import com.finite.scrapingpractise.model.UpcomingEvent

class UpcomingEventsAdapter(private val upcomingEventList:  List<UpcomingEvent>) : RecyclerView.Adapter<UpcomingEventsAdapter.EventViewHolder>() {

    private var colorCounter = (1..4).random()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.upcoming_event_item,parent,false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = upcomingEventList[position]
        holder.itemView.apply {
            //Log.d("PRI","link ${currentEvent.thumbnaillink}")
            Glide.with(holder.itemView.context).load(currentEvent.image).into(holder.upcomingEventThumbnail)

            holder.upcomingEventTitle.text = currentEvent.title
            holder.upcomingEventDate.text = "\uD83D\uDCC5  ${currentEvent.date}"
            holder.upcomingEventType.text = "${currentEvent.type}"

//            for(tag in currentEvent.tags) {
//                val chip = Chip(holder.itemView.context)
//                chip.text = tag
//                chip.textSize = 10f
//                chip.chipCornerRadius = 200f
//
//                // Set the layout parameters to wrap content
//                val params = ChipGroup.LayoutParams(
//                    ChipGroup.LayoutParams.WRAP_CONTENT,
//                    ChipGroup.LayoutParams.WRAP_CONTENT
//                )
//                chip.layoutParams = params
//                chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
//                chip.setTextColor(resources.getColor(R.color.black))
//                chip.setChipBackgroundColorResource(R.color.white)
//                chip.chipStrokeWidth = 0f
//                holder.tagsChipGroup.addView(chip)
//            }
            for (tag in currentEvent.tags) {
                val textView = TextView(holder.itemView.context)
                textView.text = tag
                textView.textSize = 10f

                textView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))

                // Create a new layout params instance
                val layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, // Replace with your desired width
                    ViewGroup.LayoutParams.WRAP_CONTENT // Replace with your desired height
                )


                layoutParams.setMargins(0, 0, resources.getDimensionPixelSize(R.dimen.tag_margin_end), resources.getDimensionPixelSize(R.dimen.tag_margin_bottom))

                textView.layoutParams = layoutParams

                // Create a drawable for rounded corners
//                val background = GradientDrawable()
//                background.cornerRadius = resources.getDimension(R.dimen.corner_radius) // Define your corner radius dimension
//                background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
//                textView.background = background
                // Set padding to control spacing
                textView.setPadding(
                    resources.getDimensionPixelSize(R.dimen.tag_padding_start),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_top),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_end),
                    resources.getDimensionPixelSize(R.dimen.tag_padding_bottom)
                )

                when (colorCounter) {
                    1 -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius) // Define your corner radius dimension
                        background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.google_blue_alpha_45))
                        textView.background = background
                    }
                    2 -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius) // Define your corner radius dimension
                        background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.google_red_alpha_45))
                        textView.background = background
                    }
                    3 -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius) // Define your corner radius dimension
                        background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.google_yellow_alpha_45))
                        textView.background = background
                    }
                    4 -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius) // Define your corner radius dimension
                        background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.google_green_alpha_45))
                        textView.background = background
                    }
                }


                // Add the TextView to the container
                holder.tagsContainer.addView(textView)
            }



            when (colorCounter) {
                1 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_blue_alpha_15))
                    holder.upcomingEventDate.setTextColor(resources.getColor(R.color.google_blue))
                    holder.icNextButton.setColorFilter(resources.getColor(R.color.google_blue))
                    holder.divider.setBackgroundColor(resources.getColor(R.color.google_blue_alpha_45))
                }

                2 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_red_alpha_15))
                    holder.upcomingEventDate.setTextColor(resources.getColor(R.color.google_red))
                    holder.icNextButton.setColorFilter(resources.getColor(R.color.google_red))
                    holder.divider.setBackgroundColor(resources.getColor(R.color.google_red_alpha_45))
                }

                3 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_yellow_alpha_15))
                    holder.upcomingEventDate.setTextColor(resources.getColor(R.color.google_yellow))
                    holder.icNextButton.setColorFilter(resources.getColor(R.color.google_yellow))
                    holder.divider.setBackgroundColor(resources.getColor(R.color.google_yellow_alpha_45))
                }

                4 -> {
                    colorCounter = 1
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_green_alpha_15))
                    holder.upcomingEventDate.setTextColor(resources.getColor(R.color.google_green))
                    holder.icNextButton.setColorFilter(resources.getColor(R.color.google_green))
                    holder.divider.setBackgroundColor(resources.getColor(R.color.google_green_alpha_45))
                }


                else -> {
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.white))
                }
            }


            holder.itemView.setOnClickListener {
                val intent = Intent(context,EventDetailActivity::class.java)
//                intent.putExtra("eventId",currentEvent.eventId)
//                intent.putExtra("eventTicket",currentEvent.ticketlink)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return upcomingEventList.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val upcomingEventThumbnail : ImageView = itemView.findViewById(R.id.imageUpcomingEvent)
        val upcomingEventTitle : TextView = itemView.findViewById(R.id.titleUpcomingEvent)
        val upcomingEventDate : TextView = itemView.findViewById(R.id.dateUpcomingEvent)
        val upcomingEventType : TextView = itemView.findViewById(R.id.typeUpcomingEvent)
        val icNextButton : ImageView = itemView.findViewById(R.id.icUpcomingEventNextButton)
//        val tagsChipGroup : ChipGroup = itemView.findViewById(R.id.tagsChipGroup)
        val tagsContainer : ViewGroup = itemView.findViewById(R.id.tagsContainer)
        val divider : View = itemView.findViewById(R.id.upcomingEventDivider)

        val cardView : CardView = itemView.findViewById(R.id.upcomingEventCardView)
    }
}