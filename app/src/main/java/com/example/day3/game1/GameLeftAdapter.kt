package com.example.day3.game1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.day3.R
import com.example.day3.wait.Wait
import kotlinx.android.synthetic.main.cell_wait.view.*

class GameLeftAdapter:RecyclerView.Adapter<GameLeftAdapter.ViewHolder>() {

    private var waitList = mutableListOf<Wait>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_wait, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return waitList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindViewHolder(waitList[position])
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun bindViewHolder(wait: Wait){

            val name = itemView.textWait
            name.text = wait.name

        }
    }

    fun update(newList: MutableList<Wait>){
        waitList = newList
        notifyDataSetChanged()
    }

}
