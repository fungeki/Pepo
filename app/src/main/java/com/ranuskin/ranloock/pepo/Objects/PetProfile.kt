package com.ranuskin.ranloock.pepo.Objects

import com.ranuskin.ranloock.pepo.Enums.Sex

class PetProfile(
    var birthDate: String,
    var signupDate: String,
    var sex: Sex,
    var color: String,
    var ownerName: String,
    var ownerEmail: String,
    var ownerAddress: String,
    var ownerCity: String,
    var phone: String,
    var petName: String,
    var lat: Double,
    var lng: Double,
    var dog_uid: String
){
    fun toHashMap(): HashMap<String, Any>{
        var hashMap = hashMapOf<String, Any>()
        hashMap["birth_date"] = this.birthDate
        hashMap["sign_up_date"] = this.signupDate
        hashMap["sex"] = this.sex.rawValue
        hashMap["color"] = this.color
        hashMap["owner_name"] = this.ownerName
        hashMap["owner_email"] = this.ownerEmail
        hashMap["owner_address"] = this.ownerAddress
        hashMap["owner_city"] = this.ownerCity
        hashMap["phone"] = this.phone
        hashMap["pet_name"] = this.petName
        hashMap["lat"] = this.lat
        hashMap["lng"] = this.lng
        hashMap["dog_uid"] = this.dog_uid
        return hashMap
    }
}
