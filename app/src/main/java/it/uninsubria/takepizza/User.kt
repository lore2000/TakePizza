package it.uninsubria.takepizza

class User(var user: String, var password: String, var email: String) {

    constructor(): this("", "","")
}