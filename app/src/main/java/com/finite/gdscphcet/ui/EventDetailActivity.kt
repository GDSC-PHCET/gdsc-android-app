package com.finite.gdscphcet.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.finite.gdscphcet.R
import com.finite.gdscphcet.databinding.ActivityEventDetailBinding
import com.finite.gdscphcet.databinding.ActivityMainBinding
import com.finite.gdscphcet.databinding.FragmentHomeBinding
import com.finite.gdscphcet.ui.viewModel.AboutViewModel
import com.finite.gdscphcet.ui.viewModel.EventDetailViewModel
import com.finite.gdscphcet.ui.viewModel.PastEvents
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EventDetailActivity : AppCompatActivity() {

    val args: EventDetailActivityArgs by navArgs()
    private val viewModel: EventDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding?.apply {
            eventActivity = this@EventDetailActivity
            eventViewModel = viewModel
        }

        val arguments = args.eventid
        setEventData(arguments)
    }



    private lateinit var database : DatabaseReference

    fun setEventData(eventId: String) {

        val posterImv : ImageView = findViewById(R.id.PosterImv)

        val tvEventTitle : TextView = findViewById(R.id.tvEventTitle)
        val tvEventDate : TextView = findViewById(R.id.tvEventDate)
        val tvEventTime : TextView = findViewById(R.id.tvEventTime)
        val tvEventMode : TextView = findViewById(R.id.tvEventMode)
        val tvAboutDetails : TextView = findViewById(R.id.tvAboutDetails)
        val videoButton: Button = findViewById(R.id.videoButton)
        val eventButton: Button = findViewById(R.id.eventButton)

        database = FirebaseDatabase.getInstance().getReference("pastEvents")
        database.child(eventId).get().addOnSuccessListener {
            if (it.exists()){

                Log.d("success", "Hua")
                val title = it.child("title").value.toString()
                val date = it.child("date").value.toString()
                val time = it.child("time").value.toString()
                val mode = it.child("mode").value.toString()
                val shortdesc = it.child("shortdesc").value.toString()
                val eventlink = it.child("eventlink").value.toString()
                val posterlink = it.child("posterlink").value.toString()
                val videolink = it.child("videolink").value.toString()

                //Toast.makeText(this, "$title : $date",Toast.LENGTH_SHORT).show()

                Glide.with(this).load(posterlink).error(R.drawable.ic_404_image).centerCrop().into(posterImv)

                tvEventTitle.text = title
                tvEventDate.text = "📅 \t\t\t:\t\t$date"
                tvEventTime.text = "⌚ \t\t\t:\t\t$time"
                tvEventMode.text = "📍 \t\t\t:\t\t$mode"
                tvAboutDetails.text = shortdesc

                videoButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videolink))
                    startActivity(intent)
                }

                eventButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventlink))
                    startActivity(intent)
                }



            }else{
                Toast.makeText(this,"does not exists",Toast.LENGTH_SHORT).show()
                Log.d("DoesNotExists", " nai hai data bhai :(")
            }

        }.addOnFailureListener{
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            Log.d("Failed","fail zhaala")
        }
    }

    fun callFinish() {
        finish()
    }
}