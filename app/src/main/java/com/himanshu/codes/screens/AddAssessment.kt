package com.himanshu.codes.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.dataFiles.UserDetails
import com.himanshu.codes.databinding.ActivityAddAssessmentBinding
import java.util.*

class AddAssessment : AppCompatActivity() {

    private lateinit var binding: ActivityAddAssessmentBinding
    private val firebaseReference = Firebase.database
    private lateinit var assessment: Assessment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAssessmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addAssessmentButton.setOnClickListener {
            assessment = Assessment(
                binding.addAssessmentTitle.text.toString(),
                binding.addAssessmentSubject.text.toString(),
                binding.addAssessmentDeadline.text.toString(),
                System.currentTimeMillis().toString()
            )
            intent.putExtra("Assessment",assessment)
            upload(assessment)
        }
        binding.addAssessmentDeadline.setOnClickListener {
            val calendar = Calendar.getInstance()

            val currYear = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.addAssessmentDeadline.setText(dat)
                },
                currYear,
                month,
                day
            )
            datePickerDialog.show()
        }
    }


    private fun upload(assessment: Assessment) {
        firebaseReference.getReference(UserDetails.UID.toString()).child("Assessment").child (assessment.id).setValue(assessment)
            .addOnSuccessListener {
                setResult(RESULT_OK,intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
            }
    }
}