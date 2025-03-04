package com.example.a35b_crud.repository

import com.example.a35b_crud.model.OrderModel
import com.example.a35b_crud.model.CartModel

interface OrderRepository {
    fun getAllOrders(userId: String, callback: (List<OrderModel>?, Boolean, String) -> Unit)
    fun checkoutCartItem(cartModel: CartModel, userId: String, callback: (Boolean, String) -> Unit)
}
