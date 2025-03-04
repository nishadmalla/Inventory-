package com.example.a35b_crud.repository

import com.example.a35b_crud.model.OrderModel
import com.example.a35b_crud.model.CartModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class OrderRepositoryImpl : OrderRepository {

    private val database = FirebaseDatabase.getInstance()
    private val ordersRef = database.reference.child("orders")

    override fun getAllOrders(userId: String, callback: (List<OrderModel>?, Boolean, String) -> Unit) {
        ordersRef.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orders = mutableListOf<OrderModel>()
                    if (snapshot.exists()){
                        for (eachOrder in snapshot.children) {
                            val order = eachOrder.getValue(OrderModel::class.java)
                            order?.let { orders.add(it) }
                        }
                        callback(orders, true, "Orders fetched successfully")
                    } else {
                        callback(emptyList(), true, "No orders found")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    callback(null, false, error.message)
                }
            })
    }

    override fun checkoutCartItem(cartModel: CartModel, userId: String, callback: (Boolean, String) -> Unit) {
        val orderId = ordersRef.push().key.toString()
        // For simplicity, use current time in millis as a string for orderDate.
        val orderDate = System.currentTimeMillis().toString()
        val orderModel = OrderModel(
            orderId = orderId,
            userId = userId,
            orderDate = orderDate,
            totalAmount = cartModel.price.toDouble() * cartModel.quantity,
            orderStatus = "Pending"
        )
        ordersRef.child(orderId).setValue(orderModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Checkout successful")
                } else {
                    callback(false, task.exception?.message ?: "Checkout failed")
                }
            }
    }
}
