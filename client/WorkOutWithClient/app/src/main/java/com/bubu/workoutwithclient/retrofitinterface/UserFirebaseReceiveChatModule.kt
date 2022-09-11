package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.bubu.workoutwithclient.firebasechat.ChatMessage
import com.bubu.workoutwithclient.firebasechat.ChatUser
import com.bubu.workoutwithclient.firebasechat.ChatVoteMessage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserFirebaseReceiveChatModule(val userId: String, val matchId: String) {
    val database =
        Firebase.database("https://workoutwith-81ab7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val msgRef = database.getReference("rooms/${matchId}/messages")

    fun receiveChat() {
        msgRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    Log.d("item",item.toString())

                    item.getValue(ChatVoteMessage::class.java)?.let { msg ->
                        //Log.d("msgtype",msg.type)
                        if(msg.type == "1"){
                            Log.d("this is Vote date", (msg as ChatVoteMessage).date)
                            Log.d("this is Vote content", (msg as ChatVoteMessage).msg)
                            Log.d("this is Vote startTime", (msg as ChatVoteMessage).startTime)
                            Log.d("this is Vote endTime", (msg as ChatVoteMessage).endTime)
                        } else {
                            Log.d("message", msg.msg.toString())
                            Log.d("timestamp", msg.timestamp.toString())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", error.message)
            }


        })
    }
    /*fun sendChat() {
        val msgId = msgRef.push().key!!
        msgRef.child(msgId).setValue(message)
    }*/
}
