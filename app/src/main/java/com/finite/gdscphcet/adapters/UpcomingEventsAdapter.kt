package com.finite.gdscphcet.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.model.UpcomingEventModel
import com.finite.gdscphcet.ui.EventDetailActivity
import com.finite.scrapingpractise.model.PastEvent
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
            holder.cardView.setCardBackgroundColor(
            when(colorCounter) {
                1 -> {
                    colorCounter++
                    resources.getColor(R.color.google_blue_background)
                }

                2 -> {
                    colorCounter++
                    resources.getColor(R.color.google_red_background)
                }

                3 -> {
                    colorCounter++
                    resources.getColor(R.color.google_yellow_background)
                }

                4 -> {
                    colorCounter = 1
                    resources.getColor(R.color.google_green_background)
                }

                else -> {
                    resources.getColor(R.color.white)
                }
            })


            holder.itemView.setOnClickListener {
                val intent = Intent(context,EventDetailActivity::class.java)
//                intent.putExtra("eventId",currentEvent.eventId)
//                intent.putExtra("eventTicket",currentEvent.ticketlink)
                holder.itemView.context.startActivity(intent)
            }
//            holder.cardView.setOnClickListener {
//                val intent = Intent(context,EventDetailActivity::class.java)
//                intent.putExtra("eventId",currentEvent.eventId)
//                intent.putExtra("eventTicket",currentEvent.ticketlink)
//                holder.itemView.context.startActivity(intent)
//            }
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

        val cardView : CardView = itemView.findViewById(R.id.upcomingEventCardView)
    }
}