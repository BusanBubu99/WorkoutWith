package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.databinding.FragmentMyProfileBinding

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

        with(binding) {
            RecyclerMyPost.adapter = adapter
            RecyclerMyPost.layoutManager = LinearLayoutManager(majorScreen)
        }
        binding.btnEditProfile.setOnClickListener {
            //majorScreen?.goEditProfileFragment()
            val direction = MyProfileFragmentDirections.actionMyProfileFragmentToEditProfileFragment2()
            findNavController().navigate(direction)
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