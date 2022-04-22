package com.himanshu.codes.screens

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.himanshu.codes.assignment.Assignment
import com.himanshu.codes.databinding.ActivityAddAssignmentBinding

class AddAssignment : AppCompatActivity() {

    private lateinit var binding: ActivityAddAssignmentBinding
    private val firebaseReference = Firebase.firestore.document(UID).collection("Assignment")
    private lateinit var assignment: Assignment
    private lateinit var UID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UID = intent.getStringExtra("UID").toString()

        binding.addAssignmentButton.setOnClickListener {
            assignment = Assignment(
                binding.addAssignmentTitle.text.toString(),
                binding.addAssignmentSubject.text.toString(),
                binding.assignmentDeadline.text.toString()
                )
            intent.putExtra("Assignment",assignment)
            upload(assignment)
        }
    }


    private fun upload(assignment: Assignment) {
        firebaseReference.add(assignment)
            .addOnSuccessListener {
                Toast.makeText(applicationContext,"Uploaded",Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK,intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
            }
    }
}