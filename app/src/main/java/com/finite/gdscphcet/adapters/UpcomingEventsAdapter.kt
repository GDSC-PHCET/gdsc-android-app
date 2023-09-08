package com.finite.gdscphcet.adapters

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.util.Log
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
import kotlin.math.log

class UpcomingEventsAdapter(private val upcomingEventList:  List<UpcomingEvent>) : RecyclerView.Adapter<UpcomingEventsAdapter.EventViewHolder>() {

    private var colorCounter = (1..4).random()
    private val baseCounter = colorCounter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.upcoming_event_item,parent,false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = upcomingEventList[position]
        holder.itemView.apply {

            Glide.with(holder.itemView.context).load(currentEvent.image).into(holder.logo)
            holder.title.text = currentEvent.title
            holder.date.text = "\uD83D\uDCC5  ${currentEvent.date}"
            holder.type.text = currentEvent.type

            for (tag in currentEvent.tags) {
                val textView = TextView(holder.itemView.context)
                textView.text = tag
                textView.textSize = 10f
                textView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))

                val layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                layoutParams.setMargins(0, 0, resources.getDimensionPixelSize(R.dimen.tag_margin_end), resources.getDimensionPixelSize(R.dimen.tag_margin_bottom))
                textView.layoutParams = layoutParams
                textView.setPadding(resources.getDimensionPixelSize(R.dimen.tag_padding_start),resources.getDimensionPixelSize(R.dimen.tag_padding_top),resources.getDimensionPixelSize(R.dimen.tag_padding_end),resources.getDimensionPixelSize(R.dimen.tag_padding_bottom))

                when (colorCounter) {
                    1 -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.google_blue_alpha_45))
                        textView.background = background
                    }
                    2 -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.google_red_alpha_45))
                        textView.background = background
                    }
                    3 -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.google_yellow_alpha_45))
                        textView.background = background
                    }
                    4 -> {
                        val background = GradientDrawable()
                        background.cornerRadius = resources.getDimension(R.dimen.corner_radius)
                        background.setColor(ContextCompat.getColor(holder.itemView.context, R.color.google_green_alpha_45))
                        textView.background = background
                    }
                }

                holder.tagsContainer.addView(textView)
            }



            when (colorCounter) {
                1 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_blue_alpha_15))
                    holder.date.setTextColor(resources.getColor(R.color.google_blue))
                    holder.textViewDetails.setTextColor(resources.getColor(R.color.google_blue))
                    holder.divider.setBackgroundResource(R.drawable.dashed_line_blue)
                }

                2 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_red_alpha_15))
                    holder.date.setTextColor(resources.getColor(R.color.google_red))
                    holder.textViewDetails.setTextColor(resources.getColor(R.color.google_red))
                    holder.divider.setBackgroundResource(R.drawable.dashed_line_red)
                }

                3 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_yellow_alpha_15))
                    holder.date.setTextColor(resources.getColor(R.color.google_yellow))
                    holder.textViewDetails.setTextColor(resources.getColor(R.color.google_yellow))
                    holder.divider.setBackgroundResource(R.drawable.dashed_line_yellow)
                }

                4 -> {
                    colorCounter = 1
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_green_alpha_15))
                    holder.date.setTextColor(resources.getColor(R.color.google_green))
                    holder.textViewDetails.setTextColor(resources.getColor(R.color.google_green))
                    holder.divider.setBackgroundResource(R.drawable.dashed_line_green)
                }


                else -> {
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.white))
                }
            }


            holder.itemView.setOnClickListener {
                val intent = Intent(context,EventDetailActivity::class.java)
                //Log.d("Testlog", "Past: url = ${currentEvent.url} and type = upcoming")
                Log.d("Testlog", "Color = ${getCurrentColor(((baseCounter + position)%4))}, Position is ${position} Position +1 is ${(position+1)}")
                intent.putExtra("color", getCurrentColor((baseCounter + position)%4))
                intent.putExtra("eventUrl", currentEvent.url)
                intent.putExtra("eventType", "upcoming")
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return upcomingEventList.size
    }

    private fun getCurrentColor(colorCounter: Int): String {
        return when (colorCounter) {
            1 -> "blue"
            2 -> "red"
            3 -> "yellow"
            0 -> "green"

            else -> "white"
        }
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val logo : ImageView = itemView.findViewById(R.id.imageUpcomingEvent)
        val title : TextView = itemView.findViewById(R.id.titleUpcomingEvent)
        val date : TextView = itemView.findViewById(R.id.dateUpcomingEvent)
        val type : TextView = itemView.findViewById(R.id.typeUpcomingEvent)
        val textViewDetails : TextView = itemView.findViewById(R.id.upcomingTextViewDetails)
        val tagsContainer : ViewGroup = itemView.findViewById(R.id.tagsContainer)
        val divider : View = itemView.findViewById(R.id.upcomingEventDivider)
        val cardView : CardView = itemView.findViewById(R.id.upcomingEventCardView)
    }
}