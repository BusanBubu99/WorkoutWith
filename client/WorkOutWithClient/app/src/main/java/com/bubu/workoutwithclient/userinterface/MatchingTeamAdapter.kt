package com.bubu.workoutwithclient.userinterface

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bubu.workoutwithclient.databinding.RecyclerMatchingTeamBinding

class MatchingTeamAdapter : RecyclerView.Adapter<MatchingTeamHolder>() {

    var listMatchingTeamData = mutableListOf<MatchingTeam>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingTeamHolder {
        val binding = RecyclerMatchingTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchingTeamHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchingTeamHolder, position: Int) {
        val matchingTeam = listMatchingTeamData.get(position)
        holder.setMatchingTeam(matchingTeam)
    }

    override fun getItemCount(): Int {
        return listMatchingTeamData.size
    }
}

class MatchingTeamHolder(val binding: RecyclerMatchingTeamBinding) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context, "클릭된 아이템=${binding.textListTitle.text}", Toast.LENGTH_LONG
            ).show()
        }
    }

    fun setMatchingTeam(matchingTeam: MatchingTeam) {
        binding.textListTitle.text = matchingTeam.title
    }
}