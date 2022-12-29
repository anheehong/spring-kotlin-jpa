package com.spring.jpa.support

class ApiReturn<T>( var data: T?, var error: String?) {


    constructor( data: T? ): this( data, "" )

    companion object{

        fun <T> of( data: T ) =  ApiReturn<T>(data)

        fun <T> errorOf(data: T, error: String? ) = ApiReturn<T>(data, error)

        fun errorOf( exception: Exception ) = ApiReturn( null, exception.message )
    }



}