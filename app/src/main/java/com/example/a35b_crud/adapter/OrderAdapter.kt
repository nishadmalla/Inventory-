package com.example.a35b_crud.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a35b_crud.R
import com.example.a35b_crud.model.OrderModel

class OrderAdapter(
    private var orderList: List<OrderModel>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtOrderId: TextView = itemView.findViewById(R.id.txtOrderId)
        val txtOrderDate: TextView = itemView.findViewById(R.id.txtOrderDate)
        val txtTotalAmount: TextView = itemView.findViewById(R.id.txtTotalAmount)
        val txtOrderStatus: TextView = itemView.findViewById(R.id.txtOrderStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.txtOrderId.text = "Order ID: ${order.orderId}"
        holder.txtOrderDate.text = "Date: ${order.orderDate}"
        holder.txtTotalAmount.text = "Total: $${order.totalAmount}"
        holder.txtOrderStatus.text = "Status: ${order.orderStatus}"
    }

    override fun getItemCount(): Int = orderList.size

    fun setOrders(orders: List<OrderModel>) {
        orderList = orders
        notifyDataSetChanged()
    }
}
