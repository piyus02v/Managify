package com.example.managify.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.managify.R
import com.example.managify.models.Board

open class BoardItemAdapter (private val context: Context, private var list : ArrayList<Board>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
            .inflate(R.layout.item_board, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            val regularFont: Typeface = Typeface.createFromAsset(holder.itemView.getContext().assets, "Raleway-Regular.ttf")
            val boldFont: Typeface = Typeface.createFromAsset(holder.itemView.getContext().assets, "Raleway-Bold.ttf")

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(holder.itemView.findViewById(R.id.item_board_iv))
            //set fonts
            holder.itemView.findViewById<TextView>(R.id.item_board_name_tv).typeface = boldFont
            holder.itemView.findViewById<TextView>(R.id.item_board_created_by_tv).typeface = regularFont
            //set text
            holder.itemView.findViewById<TextView>(R.id.item_board_name_tv).text = model.name
            holder.itemView.findViewById<TextView>(R.id.item_board_created_by_tv).text = "Created by: " + model.createdBy

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }

    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener

    }

    interface OnClickListener{
        fun onClick(position : Int, model : Board)
    }

    private class MyViewHolder(view : View): RecyclerView.ViewHolder(view){

    }
}