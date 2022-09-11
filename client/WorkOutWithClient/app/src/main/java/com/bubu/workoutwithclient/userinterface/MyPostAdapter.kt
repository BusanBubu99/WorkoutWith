package com.bubu.workoutwithclient.userinterface

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bubu.workoutwithclient.databinding.RecyclerCommunityBinding
import java.text.SimpleDateFormat

class MyPostAdapter : RecyclerView.Adapter<MyPostHolder>() {

    var listMyPostData = mutableListOf<MyPost>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostHolder {
        val binding = RecyclerCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPostHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPostHolder, position: Int) {
        val myPost = listMyPostData.get(position)
        holder.setMyPost(myPost)
    }

    override fun getItemCount(): Int {
        return listMyPostData.size
    }
}

class MyPostHolder(val binding: RecyclerCommunityBinding) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            Toast.makeText(binding.root.context
                ,"클릭된 아이템=${binding.textCommunityTitle.text}"
                , Toast.LENGTH_LONG).show()
        }
    }

    fun setMyPost(myPost: MyPost) {
        binding.textCommunityTitle.text = myPost.title
        binding.textCommunityContent.text = myPost.content

        var sdf = SimpleDateFormat("yyyy/MM/dd")
        var formattedData = sdf.format(myPost.editTime)
        binding.textCommunityTime.text = formattedData
    }
}