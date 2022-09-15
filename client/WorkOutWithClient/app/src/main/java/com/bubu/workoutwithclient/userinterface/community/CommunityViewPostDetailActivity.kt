package com.bubu.workoutwithclient.userinterface.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bubu.workoutwithclient.databinding.CommunityViewPostDetailActivityBinding
import com.bubu.workoutwithclient.userinterface.match.downloadProfilePic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommunityViewPostDetailActivity : AppCompatActivity() {

    val binding by lazy { CommunityViewPostDetailActivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pictureUri = intent.getStringExtra("pictureUri").toString()
        Log.d("imageView",pictureUri)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val editor = intent.getStringExtra("editor")
        val editTime = intent.getStringExtra("editTime")
        CoroutineScope(Dispatchers.IO).launch {
            if(pictureUri != "null") {
                val bitmap = downloadProfilePic(pictureUri)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.postPictureView.setImageBitmap(bitmap)
                }
            } else {
                Log.d("imageView", "setInit!")
                CoroutineScope(Dispatchers.Main).launch {
                    binding.postPictureView.setImageResource(0)
                }
            }
        }


        with(binding) {
            detailTitle.text = title
            detailContent.text = content
        }
    }
}