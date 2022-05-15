package com.himanshu.codes.screens

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.himanshu.codes.data.Assessment
import com.himanshu.codes.data.Assignment
import com.himanshu.codes.databinding.ActivityAddAssessmentBinding

class AddAssessment : AppCompatActivity() {

    private lateinit var binding: ActivityAddAssessmentBinding
    private val firebaseReference = Firebase.firestore
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
    }


    private fun upload(assessment: Assessment) {
        firebaseReference.collection("${uid}Assessment").add(assessment)
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