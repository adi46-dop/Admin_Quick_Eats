package com.example.adminqe.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminqe.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerName: MutableList<String>,
    private val foodImages: MutableList<String>,
    private val quantity: MutableList<String>,
    private val itemClicked: OnItemClicked,
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

interface OnItemClicked{
    fun onItemClickedListener(position: Int)
    fun onItemAcceptClickedListener(position: Int)
    fun onItemDispatchClickedListener(position: Int)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun getItemCount(): Int = customerName.size

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                txtCName.text = customerName[position]
                txtCQuantity.text = quantity[position]
                val uriString = foodImages[position]
                val image = Uri.parse(uriString)
                Glide.with(context).load(image).into(imgCustomerCart)

                btnOrderAccept.apply {
                    if(!isAccepted){
                        text = "Accept"
                    }else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if(!isAccepted){
                            text = "Dispatch"
                            isAccepted =true
                            Toast.makeText(context,"Order is Accepted",Toast.LENGTH_SHORT).show()
                            itemClicked.onItemAcceptClickedListener(position)
                        } else {
                            customerName.removeAt(adapterPosition)
                            notifyItemRemoved(position)
                            Toast.makeText(context,"Order is dispatched",Toast.LENGTH_SHORT).show()
                            itemClicked.onItemDispatchClickedListener(position)
                        }
                    }
                }
            }
            itemView.setOnClickListener {
                itemClicked.onItemClickedListener(position)
            }
        }
    }

}