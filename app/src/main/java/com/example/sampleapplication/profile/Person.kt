package com.example.sampleapplication.profile

import java.io.Serializable

data class Person(val name: String, val city: String, val age: Int) : Serializable {
}