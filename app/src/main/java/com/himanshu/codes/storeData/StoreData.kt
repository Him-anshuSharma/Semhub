package com.himanshu.codes.storeData

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.himanshu.codes.R
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.dataFiles.Assignment
import com.himanshu.codes.databinding.ActivityStoreDataBinding

class StoreData : AppCompatActivity() {

    private lateinit var binding: ActivityStoreDataBinding
    private lateinit var uid:String

    private lateinit var sharedPreferences: SharedPreferences
    private val firebaseReference = Firebase.firestore

    private var assessments: ArrayList<Assessment> = ArrayList()
    private var assignments: ArrayList<Assignment> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_data)
        uid = intent.getStringExtra("uid").toString()
        Glide.with(applicationContext).load("https://giphy.com/gifs/wait-loading-attente-hWZBZjMMuMl7sWe0x8").into(binding.loadingGif)
        getAssignments()
        getAssessments()
        saveAssignments()
        saveAssessments()
    }

    private fun getAssignments(){
        firebaseReference.collection(uid+"Assignment")
            .orderBy("assignmentDeadline", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { Assignments ->
                for (assignment in Assignments) {
                    assignments.add(Assignment(
                        assignment.getString("assignmentTitle").toString(),
                        assignment.getString("assignmentSubject").toString(),
                        assignment.getString("assignmentDeadline").toString()
                    ))
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getAssessments(){
        firebaseReference.collection(uid+"Assessment")
            .orderBy("assignmentDeadline", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { Assessments ->
                for (assessment in Assessments) {
                    assessments.add(Assessment(
                        assessment.getString("assignmentTitle").toString(),
                        assessment.getString("assignmentSubject").toString(),
                        assessment.getString("assignmentDeadline").toString()
                    ))
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveAssignments(){
        sharedPreferences = getSharedPreferences("Assignments",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(assignments)
        editor.putString("assignment list",json)
        editor.apply()
    }

    private fun saveAssessments(){
        sharedPreferences = getSharedPreferences("Assessments",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(assessments)
        editor.putString("assessment list",json)
        editor.apply()
        finish()
    }

}