package com.example.a35b_crud.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a35b_crud.model.OrderModel
import com.example.a35b_crud.model.CartModel
import com.example.a35b_crud.repository.OrderRepository

class OrderViewModel(val repository: OrderRepository) {

    private val _orders = MutableLiveData<List<OrderModel>?>()
    val orders: LiveData<List<OrderModel>?> get() = _orders

    fun getOrders(userId: String) {
        repository.getAllOrders(userId) { ordersList, success, message ->
            if (success) {
                _orders.value = ordersList
            }
        }
    }

    fun checkoutCartItem(cartModel: CartModel, userId: String, callback: (Boolean, String) -> Unit) {
        repository.checkoutCartItem(cartModel, userId, callback)
    }
}
