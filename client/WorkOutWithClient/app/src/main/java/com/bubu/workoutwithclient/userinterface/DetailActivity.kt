package com.bubu.workoutwithclient.userinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bubu.workoutwithclient.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val editor = intent.getStringExtra("editor")
        val editTime = intent.getStringExtra("editTime")

        with(binding) {
            detailTitle.text = title
            detailContent.text = content
        }
    }
}