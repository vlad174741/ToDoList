package com.example.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dataBase.DbManagerClass
import dataBase.ListItemDB
import dataBase.MyIntentConstant
import kotlinx.coroutines.DelicateCoroutinesApi

class MyAdapterRC(listMain:ArrayList<ListItemDB>, private var contextRC: Context):
    RecyclerView.Adapter<MyAdapterRC.MyViewHolder>() {

    private var listArray = listMain
    class MyViewHolder(itemView: View, private val contextVH: Context) :
        RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView = itemView.findViewById(R.id.tv_title_rc)
        private val tvTime: TextView = itemView.findViewById(R.id.tv_time_rc)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon_rc)

        @OptIn(DelicateCoroutinesApi::class)
        fun setData(item: ListItemDB){
            titleTextView.text = item.titleLI
            tvTime.text = item.time


            when(item.tag)
            {
                "empty"->ivIcon.setImageResource(0)
                "home"->ivIcon.setImageResource(R.drawable.ic_baseline_home_24)
                "shop"->ivIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24)
                "work"->ivIcon.setImageResource(R.drawable.ic_baseline_work_24)
                "weekend"->ivIcon.setImageResource(R.drawable.ic_weekend_24)
                "bank"->ivIcon.setImageResource(R.drawable.ic_bank2_15)
                "sport"->ivIcon.setImageResource(R.drawable.ic_sport2_15)

            }

            var oneClick = false

            itemView.setOnClickListener{

                fun intentInfo(){
                    oneClick = true
                    val intentActivityEdit = Intent(contextVH, EditActivity::class.java).apply {

                        putExtra(MyIntentConstant.INTENT_TITLE_KEY, item.titleLI)
                        putExtra(MyIntentConstant.INTENT_CONTENT_KEY, item.contentLI)
                        putExtra(MyIntentConstant.INTENT_URL_KEY, item.urlLI)
                        putExtra(MyIntentConstant.INTENT_ID_KEY, item.idLI)
                        putExtra(MyIntentConstant.INTENT_TAG_KEY, item.tag)
                    }
                    contextVH.startActivity(intentActivityEdit)
                }

                fun intentInfoNull(){}
                if(!oneClick){ intentInfo()}else{intentInfoNull()}

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rc_view_pattern, parent, false)
        return MyViewHolder(itemView, contextRC)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.setData(listArray[position])
    }


    override fun getItemCount(): Int { return listArray.size }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listItems:List<ListItemDB>){

        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(pos:Int, dbManager: DbManagerClass){

        notifyDataSetChanged()
        dbManager.removeItemToDb(listArray[pos].idLI. toString())
        listArray.removeAt(pos)
        notifyItemRemoved(pos)
    }

}