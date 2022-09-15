package com.bubu.workoutwithclient.retrofitinterface

/**
 * UserError Class
 * Contains specific error messages for each module
 * */

class UserError(private var messageParam : List<String>) {
    var message : List<String> = messageParam
}