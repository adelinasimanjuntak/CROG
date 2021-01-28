package com.example.loginemail_pages.Model

import kotlin.properties.Delegates

class Signup_model(){
    lateinit var nama1 : String
    lateinit var email1 : String
    lateinit var password1 : String
    lateinit var id : String
    lateinit var phonenumber1: String

    constructor(nama1: String, phonenumber1: String, email1: String, password1: String, id: String) : this(){
        this.nama1 = nama1
        this.phonenumber1 = phonenumber1
        this.email1 = email1
        this.password1 = password1
        this.id = id
    }


}