package com.example.day3

import android.content.Context

class SharedPreferences(context: Context) {
    val preference = context.getSharedPreferences("QQ", Context.MODE_PRIVATE)

    fun setName(name:String){
        preference.edit().putString("name", name).apply()
    }

    fun setPlayerId(playerid:String){
        preference.edit().putString("playerid", playerid).apply()
    }

}
