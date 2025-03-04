package com.example.a35b_crud.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a35b_crud.databinding.ActivityOrdersBinding
import com.example.a35b_crud.repository.OrderRepositoryImpl
import com.example.a35b_crud.ui.adapter.OrderAdapter
import com.example.a35b_crud.viewmodel.OrderViewModel
import com.google.firebase.auth.FirebaseAuth

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Explicit back button
        binding.btnBack.setOnClickListener { finish() }

        orderViewModel = OrderViewModel(OrderRepositoryImpl())

        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(listOf())
        binding.recyclerViewOrders.adapter = orderAdapter

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            orderViewModel.getOrders(userId)
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        orderViewModel.orders.observe(this, Observer { orders ->
            orders?.let {
                orderAdapter.setOrders(it)
            }
        })
    }
}
