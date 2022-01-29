package com.hany.txtviewlayout

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.createDeviceProtectedStorageContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.hany.txtviewlayout.databinding.ActivityViewerBinding
import com.hany.txtviewlayout.databinding.ItemRecyclerBinding

class CustomAdapter:RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<Memo>()
    //안드로이드 한 화면에서 보이는 만큼 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding= ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }
//생성된 뷰홀더에 값을 전달
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView?.context,Viewer::class.java)
            intent.putExtra("title",memo.title)
            ContextCompat.startActivity(holder.itemView.context,intent,null)
        }
    }
//리사이클러뷰에서 사용할 데이터의 총갯수 리
    override fun getItemCount(): Int {
        return listData.size
    }
}
class Holder(val binding:ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){




    fun setMemo(memo:Memo){
        binding.textView2.text = memo.title
    }
}