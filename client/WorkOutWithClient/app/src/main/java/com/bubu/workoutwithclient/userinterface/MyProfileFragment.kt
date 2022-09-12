package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.databinding.FragmentMyProfileBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


open class MyProfileFragment : Fragment() {

    lateinit var majorScreen: MajorScreen
    lateinit var binding : FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        var data : MutableList<Community> = mutableListOf<Community>()
        val communityAdapter = CommunityAdapter()
        val intent = Intent(this.context,DetailActivity::class.java)
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
                if(it.userId == userInformation.userId) {
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
                binding.RecyclerMyPost .adapter = communityAdapter
                binding.RecyclerMyPost.layoutManager = LinearLayoutManager(majorScreen)
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            val getProfileObject = UserGetProfileModule(UserGetProfileData(userInformation.userId))
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



        binding.btnEditProfile.setOnClickListener {
            majorScreen?.goNewProfileFragment()
            //val direction = MyProfileFragmentDirections.actionMyProfileFragmentToEditProfileFragment2()
            //findNavController().navigate(direction)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("request") { key, bundle ->
            bundle.getString("editId")?.let {
                binding.textProfileId.text = it
            }
        }

        val bundleId = Bundle()
        bundleId.putString("profileId", binding.textProfileId.text.toString())
        val bundleContent = Bundle()
        bundleContent.putString("profileContent", binding.textProfileContent.text.toString())

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        majorScreen = context as MajorScreen
    }

}