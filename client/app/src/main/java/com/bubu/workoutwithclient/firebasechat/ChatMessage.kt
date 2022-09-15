package com.bubu.workoutwithclient.firebasechat

import java.text.SimpleDateFormat
import java.util.*

open class ChatMessage {
    var id : String = ""
    var msg : String = ""
    var type : String = ""
    var timestamp : Long = 0

    constructor()


    constructor(id: String,msg:String,type : String) { // creator = 방 생성자
        this.id = id
        this.msg = msg
        this.type = type
        this.timestamp = System.currentTimeMillis()
    }
}
fun millsecondToDate(yourmilliseconds : Long) : String{
    //val yourmilliseconds = System.currentTimeMillis()
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm")
    val resultdate = Date(yourmilliseconds)
    return (sdf.format(resultdate))
}
