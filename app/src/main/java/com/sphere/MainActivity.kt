package com.sphere

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sphere: ImageView
    private lateinit var container: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sphere = findViewById(R.id.sphere)
        container = findViewById(R.id.container)
        val backButton: ImageButton = findViewById(R.id.back_button)

        container.setOnClickListener {
            jump()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun jump() {
        val jumpHeight = 200f
        val animation = ObjectAnimator.ofFloat(sphere, "translationY", 0f, -jumpHeight)
        animation.duration = 250
        animation.repeatCount = 1
        animation.repeatMode = ObjectAnimator.REVERSE
        animation.start()
    }
}
