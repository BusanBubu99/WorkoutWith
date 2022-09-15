package com.bubu.workoutwithclient.retrofitinterface

import android.content.Intent
import android.util.Log
import com.bubu.workoutwithclient.databinding.MatchRoomActivityBinding
import com.bubu.workoutwithclient.firebasechat.ChatMessage
import com.bubu.workoutwithclient.firebasechat.ChatVoteMessage
import com.bubu.workoutwithclient.userinterface.match.MatchRoomActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class UserFirebaseReceiveChatModule(
    val userId: String, val matchId: String, val list: MutableList<ChatMessage>,
    val adapter: MatchRoomActivity.ChatListAdapter, val binding: MatchRoomActivityBinding
) {
    val database =
        Firebase.database("https://workoutwith-81ab7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val msgRef = database.getReference("rooms/${matchId}/messages")

    fun receiveChat() {
        msgRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (item in snapshot.children) {
                    Log.d("item", item.toString())
                    item.getValue(ChatVoteMessage::class.java)?.let { msg ->
                        list.add(msg)
                        if (msg.type == "1") {
                            Log.d("this is Vote date", msg.date)
                            Log.d("this is Vote title", msg.msg)
                            Log.d("this is Vote startTime", msg.startTime)
                            Log.d("this is Vote endTime", msg.endTime)
                        } else {
                            Log.d("id", msg.id)
                            Log.d("message", msg.msg.toString())
                            Log.d("timestamp", msg.timestamp.toString())
                        }
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.notifyDataSetChanged()
                    binding.recyclerMemberChat.scrollToPosition(adapter.itemCount - 1)
                }


                Log.d("list!", list.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", error.message)
            }


        })
    }
}
