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
import com.finite.gdscphcet.model.PastEventModel
import com.finite.gdscphcet.ui.EventDetailActivity
import com.finite.scrapingpractise.model.PastEvent
import kotlin.random.Random

class PastEventsAdapter(private val pastEventsList : List<PastEvent>) : RecyclerView.Adapter<PastEventsAdapter.EventViewHolder>() {

    private var colorCounter = (1..4).random()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.past_event_item,parent,false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = pastEventsList[position]
        holder.itemView.apply {
            //Log.d("PRI","link ${currentEvent.thumbnaillink}")
            Glide.with(holder.itemView.context).load(currentEvent.image).into(holder.pastEventThumbnail)
            holder.pastEventTitle.text = currentEvent.title
            holder.pastEventDate.text = "\uD83D\uDCC5  ${currentEvent.date}"
//            holder.pastEventType.text = "\uD83D\uDCDD  ${currentEvent.type}"
            holder.pastEventType.text = "${currentEvent.type}"
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
                }
            )
            /*holder.itemView.setOnClickListener{ view ->
                val bundle = bundleOf("pastEventId" to currentEvent.eventId)
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_eventDetailActivity,bundle)
            }*/
            holder.itemView.setOnClickListener {
                val intent = Intent(context,EventDetailActivity::class.java)
                //intent.putExtra("eventId",currentEvent.eventId)
                holder.itemView.context.startActivity(intent)
            }
//            holder.cardView.setOnClickListener {
//                val intent = Intent(context,EventDetailActivity::class.java)
//                //intent.putExtra("eventId",currentEvent.eventId)
//                holder.itemView.context.startActivity(intent)
//            }
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

        val cardView : CardView = itemView.findViewById(R.id.pastEventCardView)
    }
}