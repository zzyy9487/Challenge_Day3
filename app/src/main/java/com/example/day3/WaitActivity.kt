package com.example.day3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.day3.wait.*
import kotlinx.android.synthetic.main.activity_wait.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.timer

class WaitActivity : AppCompatActivity() {

    lateinit var shared :SharedPreferences
    lateinit var adapter:WaitAdapter
    lateinit var body: Enter
    var time:Long = 0
    var endtime:Long = 0
    var position = 2
    var name = ""
    var nameList = mutableListOf<Wait>()
//    var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait)

        shared = SharedPreferences(this)
        name = shared.preference.getString("name", "").toString()
        textName.text = name

        adapter = WaitAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        val retrofit = Retrofit.Builder()
            .baseUrl("http://104.155.209.122")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(APIInterface::class.java)
        val call = apiInterface.getList()

        call.enqueue(object :retrofit2.Callback<Data>{
            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(this@WaitActivity, t.toString(), Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful){
                    if (response.code() ==200){
                    Toast.makeText(this@WaitActivity, "有其他參賽者，入局請快~", Toast.LENGTH_SHORT).show()
                    for (i in 0 until response.body()!!.players.size){
                        nameList.add(i, Wait(response.body()!!.players[i].name))
                    }
                    adapter.update(nameList)
                    }
                    else{
                        Toast.makeText(this@WaitActivity, "沒其他參賽者，會開新局哦！", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@WaitActivity, response.toString(), Toast.LENGTH_LONG).show()
                }

            }

        })



        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                position = 1
            }
            else {
                position = 2
            }
        }



        val timer = Timer(true)
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                val apiInterface2 = retrofit.create(APIInterface::class.java)
                val call2 = apiInterface2.getList()
                call2.enqueue(object :retrofit2.Callback<Data>{
                    override fun onFailure(call: Call<Data>, t: Throwable) {
                        Toast.makeText(this@WaitActivity, t.toString(), Toast.LENGTH_LONG).show()

                    }

                    override fun onResponse(call: Call<Data>, response: Response<Data>) {
                        if (response.isSuccessful){
                            if (response.code() ==200){
                                nameList.clear()
                                for (i in 0 until response.body()!!.players.size){
                                    nameList.add(i, Wait(response.body()!!.players[i].name))
                                }
                                adapter.update(nameList)
                            }
                            else{
                                nameList.clear()
                                adapter.update(nameList)
//                                Toast.makeText(this@WaitActivity, "沒其他參賽者，會開新局哦！", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            nameList.clear()
                            adapter.update(nameList)
                            Toast.makeText(this@WaitActivity, response.toString(), Toast.LENGTH_LONG).show()
                        }
                    }

                })
            }
        }
        timer.schedule(timerTask, 5000, 5000)



        btn_enter.setOnClickListener {
            time = System.currentTimeMillis()/1000
            body = Enter(name, time, position)
            val retrofit3 = Retrofit.Builder()
                .baseUrl("http://104.155.209.122")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val apiInterface3 = retrofit3.create(APIInterface::class.java)
            val call3 = apiInterface3.enter(body)

            call3.enqueue(object :retrofit2.Callback<EnterBackData>{
                override fun onFailure(call: Call<EnterBackData>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<EnterBackData>, response: Response<EnterBackData>) {
                    if (response.isSuccessful){
                        shared.setPlayerId(response.body()!!.playerId.toString())
                        val intent = Intent(this@WaitActivity, Game1Activity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@WaitActivity, "加入成功", Toast.LENGTH_LONG).show()
                        timer.cancel()
                        this@WaitActivity.finish()

                    }
                    else{
                        Toast.makeText(this@WaitActivity, response.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            })

        }

















    }

}
