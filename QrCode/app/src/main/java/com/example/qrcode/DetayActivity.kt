package com.example.qrcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.qrcode.databinding.ActivityDetayBinding

class DetayActivity : AppCompatActivity() {
    private lateinit var tasarim : ActivityDetayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasarim = ActivityDetayBinding.inflate(layoutInflater)
        setContentView(tasarim.root)

        val gelenIntent = intent.getStringExtra("mesaj")
        tasarim.textView.text = gelenIntent

    }
}