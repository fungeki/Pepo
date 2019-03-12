package com.ranuskin.ranloock.pepo.Libraries

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User

object SignedInUser {

    private var currentUser: FirebaseUser? = null
    private var user: User? = null

    fun isUserConnected(completion: ((Boolean) -> Unit)?) {
        currentUser?.let { currentUser ->
            completion?.let { completion ->
                completion(true)
            }
            return
        }
        val mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth.currentUser

        if (mUser != null) {
            currentUser = mUser
        }  else {
                completion?.let { completion ->
                        completion(false)
                }
        }
    }

    fun getUID(): String {
        currentUser?.let { user ->
            return user.uid
        }
        return ""
    }

    fun getUserName(): String{
        currentUser?.displayName?.let { display ->
            return display
        }
        return "אורח/ת"
    }
}