package com.ranuskin.ranloock.pepo.DB

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ranuskin.ranloock.pepo.Objects.PetProfile

fun createPetProfile(petProfile: PetProfile, completion: (Boolean)->Unit){
    val dbPet = FirebaseFirestore.getInstance().collection("pets_db")
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    dbPet.document(petProfile.ownerCity).collection("${petProfile.ownerName} $uid")
        .document(petProfile.petName).set(petProfile.toHashMap())
        .addOnSuccessListener {
            completion(true)
        }
        .addOnFailureListener {
            completion(false)
        }
}