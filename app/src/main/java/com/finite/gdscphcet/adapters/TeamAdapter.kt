package com.finite.gdscphcet.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.model.TeamMember
import com.finite.gdscphcet.ui.TeamViewModel
import de.hdodenhof.circleimageview.CircleImageView

class TeamAdapter(private val teamMembers: List<TeamMember>, private val teamViewModel: TeamViewModel) :
    RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

        // TODO : Hide social media icons if they are not available
        private var currentColor = teamViewModel.startColorNumber

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item_team_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = teamMembers[position]

        holder.nameTextView.text = member.name
        holder.positionTextView.text = member.position

        Glide.with(holder.itemView.context)
            .load(member.image)
            .placeholder(R.drawable.ic_asj)
            .into(holder.imageView)

        // Set background color for card
        when (currentColor) {
            1 -> {
                holder.nameTextView.setTextColor(holder.itemView.resources.getColor(R.color.google_blue))
                holder.backgroundCard.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.google_blue_alpha_15))
                currentColor++
            }
            2 -> {
                holder.nameTextView.setTextColor(holder.itemView.resources.getColor(R.color.google_red))
                holder.backgroundCard.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.google_red_alpha_15))
                currentColor++
            }
            3 -> {
                holder.nameTextView.setTextColor(holder.itemView.resources.getColor(R.color.google_yellow))
                holder.backgroundCard.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.google_yellow_alpha_15))
                currentColor++
            }
            4 -> {
                holder.nameTextView.setTextColor(holder.itemView.resources.getColor(R.color.google_green))
                holder.backgroundCard.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.google_green_alpha_15))
                currentColor = 1
            }

            else -> {
                holder.backgroundCard.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.white))
                currentColor++
            }
        }

        val context = holder.itemView.context

        if (member.linkedin == "") {
            holder.linkedInImageView.visibility = View.GONE
        } else {
            holder.linkedInImageView.visibility = View.VISIBLE
            holder.linkedInImageView.setOnClickListener {
                openLink(member.linkedin!!, context)
            }
        }
        if (member.instagram == "") {
            holder.instagramImageView.visibility = View.GONE
        } else {
            holder.instagramImageView.visibility = View.VISIBLE
            holder.instagramImageView.setOnClickListener {
                openLink(member.instagram!!, context)
            }
        }
        if (member.github == "") {
            holder.githubImageView.visibility = View.GONE
        } else {
            holder.githubImageView.visibility = View.VISIBLE
            holder.githubImageView.setOnClickListener {
                openLink(member.github!!, context)
            }
        }
        if (member.twitter == "") {
            holder.twitterImageView.visibility = View.GONE
        } else {
            holder.twitterImageView.visibility = View.VISIBLE
            holder.twitterImageView.setOnClickListener {
                openLink(member.twitter!!, context)
            }
        }
        if (member.googledev == "") {
            holder.googleDevImageView.visibility = View.GONE
        } else {
            holder.googleDevImageView.visibility = View.VISIBLE
            holder.googleDevImageView.setOnClickListener {
                openLink(member.googledev!!, context)
            }
        }
    }

    override fun getItemCount(): Int {
        return teamMembers.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: CircleImageView = itemView.findViewById(R.id.memberImage)
        val nameTextView: TextView = itemView.findViewById(R.id.memberName)
        val positionTextView: TextView = itemView.findViewById(R.id.memberPosition)
        val linkedInImageView: ImageView = itemView.findViewById(R.id.memberLinkedIn)
        val githubImageView: ImageView = itemView.findViewById(R.id.memberGithub)
        val instagramImageView: ImageView = itemView.findViewById(R.id.memberInstagram)
        val twitterImageView: ImageView = itemView.findViewById(R.id.memberTwitter)
        val googleDevImageView: ImageView = itemView.findViewById(R.id.memberGoogleDevProfile)
        val backgroundCard = itemView.findViewById<CardView>(R.id.memberCardView)
    }

    private fun openLink(link: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }
}
