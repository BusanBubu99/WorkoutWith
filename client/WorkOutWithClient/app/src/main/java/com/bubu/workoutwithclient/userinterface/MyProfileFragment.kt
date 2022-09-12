package com.bubu.workoutwithclient.userinterface

import android.content.Context
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
        val data : MutableList<MyPost> = loadMyPostData()
        var adapter = MyPostAdapter()
        adapter.listMyPostData = data


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


        with(binding) {
            RecyclerMyPost.adapter = adapter
            RecyclerMyPost.layoutManager = LinearLayoutManager(majorScreen)
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

    fun loadMyPostData() : MutableList<MyPost> {

        val data : MutableList<MyPost> = mutableListOf()
        for(no in 1..100) {
            val title = "제목 ${no}"
            val content = "내용 ${no}"
            val date = System.currentTimeMillis()

            var myPost = MyPost(title, content, date)

            data.add(myPost)
        }
        return data
    }
}