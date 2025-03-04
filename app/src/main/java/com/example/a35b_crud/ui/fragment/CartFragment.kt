package com.example.a35b_crud.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a35b_crud.databinding.FragmentCartBinding
import com.example.a35b_crud.repository.CartRepositoryImpl
import com.example.a35b_crud.repository.OrderRepositoryImpl
import com.example.a35b_crud.ui.adapter.CartAdapter
import com.example.a35b_crud.viewmodel.CartViewModel
import com.example.a35b_crud.viewmodel.OrderViewModel
import com.google.firebase.auth.FirebaseAuth

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    lateinit var cartViewModel: CartViewModel
    lateinit var orderViewModel: OrderViewModel
    lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel = CartViewModel(CartRepositoryImpl())
        orderViewModel = OrderViewModel(OrderRepositoryImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        cartAdapter = CartAdapter(
            listOf(),
            onDeleteClick = { cartItem ->
                cartViewModel.deleteCartItem(cartItem.cartId) { success, message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    if(success) {
                        cartViewModel.getAllCartItems()
                    }
                }
            },
            onCheckoutClick = { cartItem ->
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if(userId != null) {
                    orderViewModel.checkoutCartItem(cartItem, userId) { success, message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        if(success) {
                            // On successful checkout, remove the item from cart
                            cartViewModel.deleteCartItem(cartItem.cartId) { delSuccess, delMsg ->
                                if(delSuccess) cartViewModel.getAllCartItems()
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.recyclerViewCart.adapter = cartAdapter

        // Observe cart items
        cartViewModel.getAllCartItems()
        cartViewModel.allCartItems.observe(viewLifecycleOwner, Observer { cartItems ->
            cartItems?.let {
                cartAdapter.setCartItems(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
