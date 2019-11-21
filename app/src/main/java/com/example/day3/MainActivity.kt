package com.example.day3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var shared :SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shared = SharedPreferences(this)

        editName.addTextChangedListener(){
            if (editName.text.isNullOrEmpty()){
                btn_game1.isEnabled = false
                btn_game2.isEnabled = false
                btn_game3.isEnabled = false
            }
            else{
                btn_game1.isEnabled = true
                btn_game2.isEnabled = true
                btn_game3.isEnabled = true
            }
        }



        btn_game1.setOnClickListener {
            shared.setName(editName.text.toString())
            shared.setPlayerId("")
            val intent = Intent(this, WaitActivity::class.java)
            startActivity(intent)
        }

        btn_game2.setOnClickListener {
            shared.setName(editName.text.toString())
        }

        btn_game3.setOnClickListener {
            shared.setName(editName.text.toString())
        }

        btn_record.setOnClickListener {

        }



    }



}
