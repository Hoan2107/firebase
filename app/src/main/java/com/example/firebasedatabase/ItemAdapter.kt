package com.example.firebasedatabase

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
class ItemAdapter(private val itemList: ArrayList<itemDs>) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itmname: EditText = itemView.findViewById(R.id.edtname)
        val itmrate: EditText = itemView.findViewById(R.id.edtrate)
        val itmunit: EditText = itemView.findViewById(R.id.edtunit)
        val itmimg: ImageView = itemView.findViewById(R.id.itm_img)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val currentitem = itemList[position]
        holder.itmname.setText(currentitem.itemName.toString())
        holder.itmrate.setText(currentitem.itemRate.toString())
        holder.itmunit.setText(currentitem.itemUnit.toString())
        val bytes = android.util.Base64.decode(currentitem.itemImgPath,
            android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.size)
        holder.itmimg.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}
