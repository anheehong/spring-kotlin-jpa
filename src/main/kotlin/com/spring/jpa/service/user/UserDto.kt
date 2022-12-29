package com.spring.jpa.service.user

class UserDto{
    var username: String = ""
    var password: String = ""
    var displayName: String = ""
    var token: String = ""

    constructor( user: User ){
        username = user.username
        password = user._password
        displayName = user.displayName
        token = user.token
    }
}
val User.dto get() = UserDto( this )

data class UserRequestDto(
    var username: String,
    var password: String,
    var displayName: String,
) {
    var token: String = ""
}



