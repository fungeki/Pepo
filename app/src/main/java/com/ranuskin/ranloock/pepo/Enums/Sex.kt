package com.ranuskin.ranloock.pepo.Enums

enum class Sex(val hebrewName: String, val englishName: String, val rawValue: Int){
    MALE("זכר", "Male", 0),
    FEMALE("נקבה", "Female", 1);
    companion object {
        fun from(findValue: Int): Sex = Sex.values().first { it.rawValue == findValue }
    }
}