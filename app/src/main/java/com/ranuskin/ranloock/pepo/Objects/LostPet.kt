package com.ranuskin.ranloock.pepo.Objects

import java.io.Serializable

class LostPet(var name: String, var lat: Double ,var lng: Double, var imageURL: String, var date: String,
              var description: String, var appearance: String, var age: String, var phone: String, var owner: String): Serializable{

}