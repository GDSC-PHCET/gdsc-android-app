package com.finite.gdscphcet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.navArgs
import com.finite.gdscphcet.R

class EventDetailActivity : AppCompatActivity() {

    val args: EventDetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        val toasttest = args.eventid
        Toast.makeText(this,toasttest,Toast.LENGTH_SHORT).show()
    }
}