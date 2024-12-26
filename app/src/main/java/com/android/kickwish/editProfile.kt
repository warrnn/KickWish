package com.android.kickwish

import androidx.fragment.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : BottomSheetDialogFragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        sharedPreferences = requireContext().getSharedPreferences("userData", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = sharedPreferences.getString("userId", "user") ?: "user"

        val userName = view.findViewById<TextInputEditText>(R.id.userName)
        val userEmail = view.findViewById<TextInputEditText>(R.id.userEmail)
        val btnSave = view.findViewById<MaterialButton>(R.id.btnSave)

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    userName.setText(document.getString("name") ?: "")
                    userEmail.setText(document.getString("email") ?: "")
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
            }


        btnSave.setOnClickListener {
            val name = userName.text.toString()
            val email = userEmail.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val updates = mapOf("name" to name, "email" to email)

                db.collection("users").document(userId).update(updates)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()

                        val activity = activity as ProfileActivity
                        activity.getProfilefromDatabase(FirebaseFirestore.getInstance())

                        dismiss()
                    }
                    .addOnFailureListener { e ->
                        Log.e("EditProfile", "Error updating profile", e)
                        Toast.makeText(context, "Error updating profile", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
