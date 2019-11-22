package com.example.day3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.day3.game1.Ans
import com.example.day3.game1.GameLeftAdapter
import com.example.day3.game1.GameRightAdapter
import com.example.day3.wait.Data
import com.example.day3.wait.Players
import com.example.day3.wait.Wait
import kotlinx.android.synthetic.main.activity_game1.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.timer


class Game1Activity : AppCompatActivity() {

    lateinit var shared :SharedPreferences
    lateinit var leftAdapter:GameLeftAdapter
    lateinit var rightAdapter:GameRightAdapter
    var endtime :Int = 0
    var leftList = mutableListOf<Wait>()
    var rightList = mutableListOf<Wait>()
    var left = mutableListOf<Players>()
    var right = mutableListOf<Players>()
    var gamerData = mutableListOf<Players>()
    var name = ""
    var ansStatus:Int = 0
    lateinit var timer:Timer

//    private var repeatTaskTime:Long = 5000
//    private var taskHandler = Handler()
//    private var runnable = object:Runnable{
//        override fun run() {
//            upupdate()
//        }
//
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1)
        shared = SharedPreferences(this)
        name = shared.preference.getString("name", "").toString()
        val playerId = shared.preference.getString("playerid", "").toString()

        leftAdapter = GameLeftAdapter()
        rightAdapter = GameRightAdapter()
        recyclerLeft.layoutManager = LinearLayoutManager(this)
        recyclerRight.layoutManager = LinearLayoutManager(this)
        recyclerLeft.adapter = leftAdapter
        recyclerRight.adapter = rightAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("http://104.155.209.122")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(APIInterface::class.java)
        val call = apiInterface.getList()

        call.enqueue(object :retrofit2.Callback<Data>{
            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(this@Game1Activity, t.toString(), Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if(data!= null){
                        val list = data.players
                        endtime = data.endTime
                        val nowtime = System.currentTimeMillis()/1000
                        val time = endtime - nowtime
                        textTime.text = time.toString()
                        gamerData.addAll(list)
                        if (!list.isNullOrEmpty()){
                            left.addAll(list.filter { it.position == 1 })
                            right.addAll(list.filter { it.position == 2 })
                            if (left.size > 0){
                                for (i in 0 until left.size){
                                    leftList.add(i, Wait(left[i].name))
                                }
                                leftAdapter.update(leftList)
                            }
                            if(right.size > 0){
                                for (i in 0 until right.size){
                                    rightList.add(i, Wait(right[i].name))
                                }
                                rightAdapter.update(rightList)
                            }
                        }

                    }

                }
                else{
                    Toast.makeText(this@Game1Activity, response.toString(), Toast.LENGTH_LONG).show()
                }

            }

        })


        timer = Timer(true)
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                if(ansStatus == 0){
                    val retrofit2 = Retrofit.Builder()
                        .baseUrl("http://104.155.209.122")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val apiInterface2 = retrofit2.create(APIInterface::class.java)
                    val call2 = apiInterface2.getList()
                    call2.enqueue(object :retrofit2.Callback<Data>{
                        override fun onFailure(call: Call<Data>, t: Throwable) {
                        }
                        override fun onResponse(call: Call<Data>, response: Response<Data>) {
                            if (response.code() == 200) {
                                val data = response.body()
                                if (data != null) {
                                    val nowtime = System.currentTimeMillis()/1000
                                    val time = endtime - nowtime
                                    textTime.text = time.toString()
                                    val list = data.players
                                    if (list == gamerData) {
                                    } else {
                                        if (!list.isNullOrEmpty()) {
                                            left.clear()
                                            right.clear()
                                            left.addAll(list.filter { it.position == 1 })
                                            right.addAll(list.filter { it.position == 2 })
                                            leftList.clear()
                                            rightList.clear()
                                            if (left.size > 0) {
                                                for (i in 0 until left.size) {
                                                    leftList.add(i, Wait(left[i].name))
                                                }
                                                leftAdapter.update(leftList)
                                            }
                                            if (right.size > 0) {
                                                for (i in 0 until right.size) {
                                                    rightList.add(i, Wait(right[i].name))
                                                }
                                                rightAdapter.update(rightList)
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                textTime.text = "0"
                                ansStatus = 1
                            }
                        }
                    })
                }
                else{
                    val retrofit3 = Retrofit.Builder()
                        .baseUrl("http://104.155.209.122")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val apiInterface3 = retrofit3.create(APIInterface::class.java)
                    val call3 = apiInterface3.ans(playerId)
                    call3.enqueue(object :retrofit2.Callback<Ans>{
                        override fun onFailure(call: Call<Ans>, t: Throwable) {
                        }
                        override fun onResponse(call: Call<Ans>, response: Response<Ans>) {
                            if (response.code() ==200){
                                val ansdata = response.body()
                                if (response.body()!!.isWinner){
                                    Toast.makeText(this@Game1Activity, (ansdata!!.yourName +"  恭喜獲勝！"), Toast.LENGTH_LONG).show()
                                    timer.cancel()
                                }
                                else{
                                    Toast.makeText(this@Game1Activity, (ansdata!!.yourName +"  哈哈輸了！"), Toast.LENGTH_LONG).show()
                                    timer.cancel()
                                }
                                if (ansdata.answer == 1){
                                    recyclerRight.alpha = 0.5.toFloat()
                                }
                                else{
                                    recyclerLeft.alpha = 0.5.toFloat()
                                }
                            }
                        }
                    })
                }

            }
        }
        timer.schedule(timerTask, 2000, 2000)

//        val timer2 = Timer(true)
//        val timerTask2: TimerTask = object : TimerTask() {
//            override fun run() {
//                val nowtime = System.currentTimeMillis()/1000
//                val time = endtime - nowtime
//                textTime.text = time.toString()
//            }
//        }
//        timer2.schedule(timerTask2, 1000, 1000)

    }

//    private fun upupdate() {
//
//        val apiInterface = retrofit.create(APIInterface::class.java)
//        val call = apiInterface.getList()
//
//        call.enqueue(object :retrofit2.Callback<Data>{
//            override fun onFailure(call: Call<Data>, t: Throwable) {
//            }
//
//            override fun onResponse(call: Call<Data>, response: Response<Data>) {
//                if (response.isSuccessful){
//                    val data = response.body()
//                    if(data!= null){
//                        var list = data.players
//                        textTime.text = data!!.endTime.toString()
//                        if (!list.isNullOrEmpty()){
//                            left.addAll(list.filter { it.position == 1 })
//                            right.addAll(list.filter { it.position == 2 })
//                            if (left.size > 0){
//                                for (i in 0 until left.size){
//                                    leftList.add(i, Wait(left[i].name))
//                                }
//                                leftAdapter.update(leftList)
//                            }
//                            if(right.size > 0){
//                                for (i in 0 until right.size){
//                                    rightList.add(i, Wait(right[i].name))
//                                }
//                                rightAdapter.update(rightList)
//                            }
//                        }
//                    }
//                }
//            }
//        })
//        taskHandler.postDelayed(runnable,repeatTaskTime)
//
//    }


    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
        shared.setPlayerId("")
        shared.setName("")
        this@Game1Activity.finish()
    }

}
