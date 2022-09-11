package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.databinding.FragmentCommunityBinding


class  CommunityFragment : Fragment() {

    lateinit var majorScreen: MajorScreen
    lateinit var binding : FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        //val data : MutableList<Community> = loadCommunityData()
        //var adapter = CommunityAdapter()
        //adapter.listCommunityData = data
        with(binding) {
            //RecyclerCommunity.adapter = adapter
            //RecyclerCommunity.layoutManager = LinearLayoutManager(majorScreen)
        }
        binding.btnGoPost.setOnClickListener {
            val direction = CommunityFragmentDirections.actionCommunityFragmentToPostNewFragment()
            findNavController().navigate(direction)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data1 = loadCommunityData()
        val communityAdapter = CommunityAdapter()
        communityAdapter.listCommunityData = data1
        binding.RecyclerCommunity.adapter = communityAdapter
        binding.RecyclerCommunity.layoutManager = LinearLayoutManager(this.activity)

        val intent = Intent(this.context, DetailActivity::class.java)
        communityAdapter.setOnItemClickListner(object: CommunityAdapter.OnItemClickListner{
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("title", data1[position].title)
                intent.putExtra("content", data1[position].content)
                intent.putExtra("editor", data1[position].editor)
                intent.putExtra("editTime", data1[position].editTime)
                startActivity(intent)
            }
        })


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        majorScreen = context as MajorScreen
    }

    fun loadCommunityData() : MutableList<Community> {

        val data : MutableList<Community> = mutableListOf()
        for(no in 1..100) {
            val title = "제목 ${no}"
            val content = "내용 ${no}"
            val editor = "작성자 ${no}"
            val date = System.currentTimeMillis()

            var community = Community(title, content, editor, date)

            data.add(community)
        }
        return data
    }
}