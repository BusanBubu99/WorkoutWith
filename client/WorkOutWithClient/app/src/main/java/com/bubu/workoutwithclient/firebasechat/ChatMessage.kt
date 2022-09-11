package com.bubu.workoutwithclient.firebasechat

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
