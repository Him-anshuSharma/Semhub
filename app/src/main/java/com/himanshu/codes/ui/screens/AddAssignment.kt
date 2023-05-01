package com.himanshu.codes.ui.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.himanshu.codes.dataFiles.Assignment
import com.himanshu.codes.databinding.ActivityAddAssignmentBinding
import java.util.*

class AddAssignment : AppCompatActivity() {

    private lateinit var binding: ActivityAddAssignmentBinding
    private val firebaseReference = Firebase.database
    private lateinit var assignment: Assignment
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.getStringExtra("UID").toString()

        binding.addAssignmentButton.setOnClickListener {
            assignment = Assignment(
                binding.addAssignmentTitle.text.toString(),
                binding.addAssignmentSubject.text.toString(),
                binding.assignmentDeadline.text.toString()
                )
            intent.putExtra("Assignment",assignment)
            upload(assignment)
        }
        binding.assignmentDeadline.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.assignmentDeadline.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    private fun upload(assignment: Assignment) {
        firebaseReference.getReference("${uid}").child("Assignment").child ("${System.currentTimeMillis()}").setValue(assignment)
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