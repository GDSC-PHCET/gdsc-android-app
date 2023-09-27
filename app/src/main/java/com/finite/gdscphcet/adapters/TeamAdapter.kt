package com.finite.gdscphcet.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.model.TeamMember
import de.hdodenhof.circleimageview.CircleImageView

class TeamAdapter(private val teamMembers: List<TeamMember>) :
    RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

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
            .placeholder(R.drawable.ic_google_developers)
            .into(holder.imageView)

        val context = holder.itemView.context

        // Set click listeners for social media links
        holder.linkedInImageView.setOnClickListener {
            openLink(member.linkedin!!, context)
        }

        holder.instagramImageView.setOnClickListener {
            openLink(member.instagram!!, context)
        }

        holder.githubImageView.setOnClickListener {
            openLink(member.github!!, context)
        }

        holder.twitterImageView.setOnClickListener {
            openLink(member.twitter!!, context)
        }

        holder.googleDevImageView.setOnClickListener {
            openLink(member.googledev!!, context)
        }
    }

    override fun getItemCount(): Int {
        return teamMembers.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views in your grid item layout (ImageViews, TextViews, etc.)
        val imageView: CircleImageView = itemView.findViewById(R.id.memberImage)
        val nameTextView: TextView = itemView.findViewById(R.id.memberName)
        val positionTextView: TextView = itemView.findViewById(R.id.memberPosition)
        val linkedInImageView: ImageView = itemView.findViewById(R.id.memberLinkedIn)
        val githubImageView: ImageView = itemView.findViewById(R.id.memberGithub)
        val instagramImageView: ImageView = itemView.findViewById(R.id.memberInstagram)
        val twitterImageView: ImageView = itemView.findViewById(R.id.memberTwitter)
        val googleDevImageView: ImageView = itemView.findViewById(R.id.memberGoogleDevProfile)
    }

    private fun openLink(link: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }
}
