package com.finite.gdscphcet.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.finite.gdscphcet.R

class TrackDetailActivity : AppCompatActivity() {
    private val args by navArgs<TrackDetailActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutPosition())
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#6A0DAD")))
        supportActionBar?.setTitle(args.position)
        setTextView()

    }


    private fun getLayoutPosition(): Int {

        return when(args.position) {

            R.string.android_dev -> {
               // setTextView()
                R.layout.android_details
            }

            R.string.flutter_dev -> {
               // setTextView()
                R.layout.flutter_details
            }

            R.string.web_dev -> {
              //  setTextView()
                R.layout.web_dev
            }

            R.string.machine_learning -> {
              //  setTextView()
                R.layout.graphic_design_res
            }

            R.string.ar_vr_res -> {
            //    setTextView()
                R.layout.augmented_virtual_res
            }

            R.string.open_source -> {
             //   setTextView()
                R.layout.open_source
            }

            R.string.cloud_res -> {
             //   setTextView()
                R.layout.cloud_resources
            }

            R.string.graphic_design -> {
            //    setTextView()
                R.layout.graphic_design_res
            }

            else ->
                R.layout.activity_track_detail


        }
    }

    private fun setTextView(): Unit {

        if(args.position != R.string.much_more)
        findViewById<TextView>(R.id.descrip_text).text= getString(args.position)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }
}