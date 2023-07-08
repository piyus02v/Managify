package com.example.managify.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.managify.R
import com.example.managify.models.User
import com.example.managify.utils.Constants

open class MemberItemAdapter (private val context: Context, private var list: ArrayList<User>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_member, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            val regularFont: Typeface = Typeface.createFromAsset(holder.itemView.getContext().assets, "Raleway-Regular.ttf")
            val boldFont: Typeface = Typeface.createFromAsset(holder.itemView.getContext().assets, "Raleway-Bold.ttf")
            holder.itemView.findViewById<TextView>(R.id.member_name_tv).typeface = boldFont
            holder.itemView.findViewById<TextView>(R.id.member_email_tv).typeface = regularFont
            holder.itemView.findViewById<TextView>(R.id.member_name_tv).text = model.name
            holder.itemView.findViewById<TextView>(R.id.member_email_tv).text = model.email
            Glide
                .with(context)
                .load(model.image)
                .fitCenter()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.itemView.findViewById(R.id.member_image_iv))

            if(model.selected){
                holder.itemView.findViewById<ImageView>(R.id.selected_member_iv).visibility = View.VISIBLE
            }else{
                holder.itemView.findViewById<ImageView>(R.id.selected_member_iv).visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    if(model.selected){
                        onClickListener!!.onClick(position,model, Constants.UN_SELECT)
                    }else{
                        onClickListener!!.onClick(position,model,Constants.SELECT)
                    }
                }
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}