package it.uninsubria.takepizza

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class User(var name: String,var password: String, var email: String) {
    var id: Int = -1
    constructor(): this("", "","")
    constructor(name: String,password:String, email: String, id: Int): this(name,password, email){
        this.id = id
    }
    override fun toString(): String{
        return "$email-$password"
    }
    fun set(user2: User){
        id = user2.id
        name = user2.name
        email = user2.name
    }
}