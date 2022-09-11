package com.bubu.workoutwithclient.userinterface

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bubu.workoutwithclient.databinding.RecyclerCommunityBinding
import java.text.SimpleDateFormat

class CommunityAdapter : RecyclerView.Adapter<CommunityAdapter.CommunityHolder>() {

	interface OnItemClickListner {
		fun onItemClick(view: View, position: Int)
	}

	private lateinit var itemClickListner: OnItemClickListner

	fun setOnItemClickListner(onItemClickListner: OnItemClickListner) {
		itemClickListner = onItemClickListner
	}

	var listCommunityData = mutableListOf<Community>()
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityHolder {
		val binding = RecyclerCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return CommunityHolder(binding)
	}

	override fun onBindViewHolder(holder: CommunityHolder, position: Int) {
		val community = listCommunityData.get(position)
		holder.setCommunity(community)
	}

	override fun getItemCount(): Int {
		return listCommunityData.size
	}

	inner class CommunityHolder(val binding: RecyclerCommunityBinding) : RecyclerView.ViewHolder(binding.root) {
		init {
			binding.root.setOnClickListener {
				val pos = adapterPosition
				if(pos != RecyclerView.NO_POSITION && itemClickListner != null) {
					itemClickListner.onItemClick(binding.root, pos)
				}
			}
		}

		fun setCommunity(community: Community) {
			binding.textCommunityTitle.text = community.title
			binding.textCommunityContent.text = community.content
			binding.textCommunityEditor.text = community.editor

			var sdf = SimpleDateFormat("yyyy/MM/dd")
			var formattedData = sdf.format(community.editTime)
			binding.textCommunityTime.text = formattedData
		}
	}
}