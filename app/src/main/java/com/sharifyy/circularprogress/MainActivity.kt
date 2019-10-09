package com.sharifyy.circularprogress

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val random = Random(100)

        button.setOnClickListener {
            if (editText.text.isNotEmpty()) progress_circular.setProgressWithAnimation(editText.text.toString().toFloat())
            else progress_circular.setProgressWithAnimation(random.nextFloat() * 100)
        }

    }
}
