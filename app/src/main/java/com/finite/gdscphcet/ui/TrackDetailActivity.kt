package com.finite.gdscphcet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.finite.gdscphcet.R

class TrackDetailActivity : AppCompatActivity() {
    private val args by navArgs<TrackDetailActivityArgs>()

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_detail)

        toolbar=findViewById(R.id.toolbar_trackDetailActivity)
        setSupportActionBar(toolbar)
        toolbar.title="GDSC PHCET"


        val position = args.position
    }
}