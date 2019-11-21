package com.example.day3

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import com.example.day3.wait.Data
import com.example.day3.wait.Wait
import kotlinx.android.synthetic.main.activity_game1.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OtherTask: AsyncTask<Context, Int, Int>() {

    val game1 = Game1Activity()

    override fun doInBackground(vararg params: Context?): Int {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://104.155.209.122")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(APIInterface::class.java)
        val call = apiInterface.getList()

        call.enqueue(object :retrofit2.Callback<Data>{
            override fun onFailure(call: Call<Data>, t: Throwable) {
            }

            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful){
                    val data = response.body()
                    if(data!= null){
                        var list = data.players
                        game1.textTime.text = data!!.endTime.toString()
                        if (!list.isNullOrEmpty()){
                            game1.left.addAll(list.filter { it.position == 1 })
                            game1.right.addAll(list.filter { it.position == 2 })
                            if (game1.left.size > 0){
                                for (i in 0 until game1.left.size){
                                    game1.leftList.add(i, Wait(game1.left[i].name))
                                }
                                game1.leftAdapter.update(game1.leftList)
                            }
                            if(game1.right.size > 0){
                                for (i in 0 until game1.right.size){
                                    game1.rightList.add(i, Wait(game1.right[i].name))
                                }
                                game1.rightAdapter.update(game1.rightList)
                            }
                        }
                    }
                }
            }
        })
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}