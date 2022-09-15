package com.bubu.workoutwithclient.userinterface.match

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.databinding.MatchUserProfileActivityBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import com.bubu.workoutwithclient.userinterface.community.Community
import com.bubu.workoutwithclient.userinterface.community.CommunityAdapter
import com.bubu.workoutwithclient.userinterface.community.getCommunityList
import com.bubu.workoutwithclient.userinterface.community.CommunityViewPostDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MatchUserProfileActivity : AppCompatActivity() {

    val binding by lazy { MatchUserProfileActivityBinding.inflate(layoutInflater) }
    lateinit var mContext : MatchUserProfileActivity
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
                    setTitle("${result.name}"+"님의 프로필")
                }
            } else if(result is UserError) {

            } else {

            }
        }

        var data : MutableList<Community> = mutableListOf<Community>()
        val communityAdapter = CommunityAdapter()
        val intent = Intent(this, CommunityViewPostDetailActivity::class.java)
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
                val bitmap: Bitmap
                if (it.userId == targetId) {
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

    /*override fun onResume() {
        setTitle("${}"+"님의 프로필")
        super.onResume()
    }*/
}