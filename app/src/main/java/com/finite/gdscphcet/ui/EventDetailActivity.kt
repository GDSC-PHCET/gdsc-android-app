package com.finite.gdscphcet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.navArgs
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

                Toast.makeText(this, "$title : $date",Toast.LENGTH_SHORT).show()










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