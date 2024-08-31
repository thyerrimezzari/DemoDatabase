package com.example.demodatabase.datasql

data class Usuario(
    val id: Int?, val name: String?, val email: String?
) {
    override fun toString(): String {
        return "$id:$name:$email"
    }
}