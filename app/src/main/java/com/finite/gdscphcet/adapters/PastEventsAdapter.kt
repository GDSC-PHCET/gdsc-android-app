package com.finite.gdscphcet.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.ui.EventDetailActivity
import com.finite.scrapingpractise.model.PastEvent

class PastEventsAdapter(private val pastEventsList : List<PastEvent>) : RecyclerView.Adapter<PastEventsAdapter.EventViewHolder>() {

    private var colorCounter = (1..4).random()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.past_event_item,parent,false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = pastEventsList[position]
        holder.itemView.apply {

            Glide.with(holder.itemView.context).load(currentEvent.image).into(holder.pastEventThumbnail)
            holder.pastEventTitle.text = currentEvent.title
            holder.pastEventDate.text = "\uD83D\uDCC5  ${currentEvent.date}"

            holder.pastEventType.text = currentEvent.type

            when (colorCounter) {
                1 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_blue_alpha_15))
                    holder.pastEventDate.setTextColor(resources.getColor(R.color.google_blue))
                    holder.icNextButton.setColorFilter(resources.getColor(R.color.google_blue))
//                    holder.divider.setBackgroundColor(resources.getColor(R.color.google_blue_alpha_45))
                    holder.divider.setBackgroundResource(R.drawable.dashed_vertical_line_blue)
                }

                2 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_red_alpha_15))
                    holder.pastEventDate.setTextColor(resources.getColor(R.color.google_red))
                    holder.icNextButton.setColorFilter(resources.getColor(R.color.google_red))
//                    holder.divider.setBackgroundColor(resources.getColor(R.color.google_red_alpha_45))
                    holder.divider.setBackgroundResource(R.drawable.dashed_vertical_line_red)
                }

                3 -> {
                    colorCounter++
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_yellow_alpha_15))
                    holder.pastEventDate.setTextColor(resources.getColor(R.color.google_yellow))
                    holder.icNextButton.setColorFilter(resources.getColor(R.color.google_yellow))
//                    holder.divider.setBackgroundColor(resources.getColor(R.color.google_yellow_alpha_45))
                    holder.divider.setBackgroundResource(R.drawable.dashed_vertical_line_yellow)
                }

                4 -> {
                    colorCounter = 1
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.google_green_alpha_15))
                    holder.pastEventDate.setTextColor(resources.getColor(R.color.google_green))
                    holder.icNextButton.setColorFilter(resources.getColor(R.color.google_green))
//                    holder.divider.setBackgroundColor(resources.getColor(R.color.google_green_alpha_45))
                    holder.divider.setBackgroundResource(R.drawable.dashed_vertical_line_green)
                }


                else -> {
                    holder.cardView.setCardBackgroundColor(resources.getColor(R.color.white))
                }
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context,EventDetailActivity::class.java)
                //intent.putExtra("eventId",currentEvent.eventId)
                holder.itemView.context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return pastEventsList.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val pastEventThumbnail : ImageView = itemView.findViewById(R.id.imagePastEvent)
        val pastEventTitle : TextView = itemView.findViewById(R.id.titlePastEvent)
        val pastEventDate : TextView = itemView.findViewById(R.id.datePastEvent)
        val pastEventType : TextView = itemView.findViewById(R.id.typePastEvent)
        val icNextButton : ImageView = itemView.findViewById(R.id.icPastEventNextButton)
        val divider : View = itemView.findViewById(R.id.pastEventDivider)

        val cardView : CardView = itemView.findViewById(R.id.pastEventCardView)
    }
}