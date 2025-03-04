package com.example.a35b_crud.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a35b_crud.databinding.ActivitySettingsBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Explicit back button
        binding.btnBack.setOnClickListener { finish() }

        binding.txtSettingsTitle.text = "Settings"

        binding.btnUpdatePassword.setOnClickListener {
            val oldPassword = binding.editOldPassword.text.toString().trim()
            val newPassword = binding.editNewPassword.text.toString().trim()

            if(oldPassword.isEmpty()) {
                Toast.makeText(this, "Please enter your old password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(newPassword.isEmpty() || newPassword.length < 6) {
                Toast.makeText(this, "New password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = FirebaseAuth.getInstance().currentUser
            if(user != null && user.email != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
                user.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                    if(reAuthTask.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if(updateTask.isSuccessful) {
                                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Failed to update password: ${updateTask.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
