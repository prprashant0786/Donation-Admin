package com.example.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FormDataAdapter(
    private val formDataList: List<FormData>,
    private val onItemClick: (FormData) -> Unit // Added onItemClick listener
) : RecyclerView.Adapter<FormDataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        val itemQuantityTextView: TextView = itemView.findViewById(R.id.itemQuantityTextView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_form_data, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formData = formDataList[position]
        holder.nameTextView.text = "Name: ${formData.name}"
        holder.addressTextView.text = "Address: ${formData.address}"
        holder.phoneNumberTextView.text = "Phone Number: ${formData.phoneNumber}"
        holder.itemNameTextView.text = "Item Name: ${formData.itemName}"
        holder.itemQuantityTextView.text = "Item Quantity: ${formData.itemQuantity}"

        holder.itemView.setOnClickListener {
            onItemClick(formData)
        }

    }

    override fun getItemCount(): Int {
        return formDataList.size
    }
}
