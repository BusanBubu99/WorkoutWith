package com.bubu.workoutwithclient.userinterface

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bubu.workoutwithclient.databinding.FragmentPostNewBinding


class PostNewFragment : Fragment() {

    lateinit var majorScreen: MajorScreen
    lateinit var binding: FragmentPostNewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostNewBinding.inflate(inflater, container, false)
        binding.btnPost.setOnClickListener {
            val bundle = bundleOf("valueKey" to "테스트")
            setFragmentResult("request", bundle)
            val direction = PostNewFragmentDirections.actionPostNewFragmentToCommunityFragment()
            findNavController().navigate(direction)
        }
        return binding.root
    }

    fun getdata() : MutableList<Community> {
        val data : MutableList<Community> = mutableListOf()
        val title = binding.editPostTitle.text.toString()
        val content = binding.editPostContent.text.toString()
        val editor = "작성자"
        val date = System.currentTimeMillis()

        var community = Community(title, content, editor, date)
        data.add(community)
        return data
    }
}