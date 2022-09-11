package com.bubu.workoutwithclient.firebasechat

class ChatVoteMessage : ChatMessage {
    var startTime : String = ""
    var endTime : String = ""
    var date : String = ""


    constructor()


    constructor(id: String,startTime:String, endTime: String,date : String,content:String,type : String = "1") { // creator = 방 생성자
        super.id = id
        this.startTime = startTime
        this.endTime = endTime
        this.date = date
        super.msg = content
        super.type = type
        super.timestamp = System.currentTimeMillis()
    }
}