package com.bubu.workoutwithclient.userinterface

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.databinding.ActivityUserProfileBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserProfileActivity : AppCompatActivity() {

    val binding by lazy { ActivityUserProfileBinding.inflate(layoutInflater) }
    lateinit var mContext : UserProfileActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(binding.root)
        val receivedIntent = intent
        var targetId = receivedIntent.getStringExtra("targetId").toString()
        CoroutineScope(Dispatchers.Default).launch {
            val getProfileObject = UserGetProfileModule(UserGetProfileData(targetId))
            val result = getProfileObject.getApiData()
            if(result is UserGetProfileResponseData) {
                Log.d("result!!!!",result.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        downloadProfilePic(result.profilePic)
                    }
                    binding.profileImage.setImageBitmap(bitmap)
                    binding.textProfileContent.text = result.tags
                    binding.textProfileId.text = result.name
                }
            } else if(result is UserError) {

            } else {

            }
        }

        //1. Intent
        var data : MutableList<Community> = mutableListOf<Community>()
        val communityAdapter = CommunityAdapter()
        val intent = Intent(this, DetailActivity::class.java)
        communityAdapter.setOnItemClickListner(object : CommunityAdapter.OnItemClickListner{
            override fun onItemClick(view: View, position: Int) {
                if(data[position].pictureUri != null)
                    intent.putExtra("pictureUri",data[position].pictureUri)
                else
                    intent.putExtra("pictureUri", "null")
                intent.putExtra("title", data[position].title)
                intent.putExtra("content", data[position].content)
                intent.putExtra("editor", data[position].editor)
                intent.putExtra("editTime", data[position].editTime)
                startActivity(intent)
            }
        })
        CoroutineScope(Dispatchers.Default).launch {
            val communityData = getCommunityList() as List<UserGetCommunityListResponseData>
            communityData.forEach {
                val bitmap : Bitmap
                if(it.userId == targetId) {
                    if (it.picture != null) {
                        bitmap = withContext(Dispatchers.IO) {
                            downloadProfilePic(it.picture)
                        }
                        data.add(
                            Community(
                                it.title,
                                it.contents,
                                it.userId,
                                it.timestamp,
                                bitmap,
                                it.picture
                            )
                        )
                    } else {
                        data.add(
                            Community(
                                it.title,
                                it.contents,
                                it.userId,
                                it.timestamp,
                                null,
                                it.picture
                            )
                        )
                    }
                }
            }
            communityAdapter.listCommunityData = data
            CoroutineScope(Dispatchers.Main).launch {
                binding.RecyclerPostList.adapter = communityAdapter
                binding.RecyclerPostList.layoutManager = LinearLayoutManager(mContext)
            }
        }



    }
}