package com.himanshu.codes.ui.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.databinding.ActivityAddAssessmentBinding
import java.util.*

class AddAssessment : AppCompatActivity() {

    private lateinit var binding: ActivityAddAssessmentBinding
    private val firebaseReference = Firebase.database
    private lateinit var assessment: Assessment
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAssessmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.getStringExtra("UID").toString()

        binding.addAssessmentButton.setOnClickListener {
            assessment = Assessment(
                binding.addAssessmentTitle.text.toString(),
                binding.addAssessmentSubject.text.toString(),
                binding.addAssessmentDeadline.text.toString()
            )
            intent.putExtra("Assessment",assessment)
            upload(assessment)
        }
        binding.addAssessmentDeadline.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.addAssessmentDeadline.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }


    private fun upload(assessment: Assessment) {
        firebaseReference.getReference(uid).child("Assessment").child ("${System.currentTimeMillis()}").setValue(assessment)
            .addOnSuccessListener {
                setResult(RESULT_OK,intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
            }
    }
}