package com.himanshu.codes.storeData

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.dataFiles.Assignment
import com.himanshu.codes.databinding.ActivityStoreDataBinding
import com.himanshu.codes.screens.HomeScreen
import com.himanshu.codes.time.Time

class StoreData : AppCompatActivity() {

    private lateinit var binding: ActivityStoreDataBinding
    private lateinit var uid:String
    private lateinit var name: String

    private lateinit var sharedPreferences: SharedPreferences
    private val firebaseReference = Firebase.firestore
    private val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday")
    private lateinit var timetableDatabase :DatabaseReference

    private var assessments: ArrayList<Assessment> = ArrayList()
    private var assignments: ArrayList<Assignment> = ArrayList()
    private var classesList: java.util.ArrayList<Time> = java.util.ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = intent.getStringExtra("UID").toString()
        name = intent.getStringExtra("NAME").toString()
        Toast.makeText(applicationContext,"$uid \n $name",Toast.LENGTH_SHORT).show()
        timetableDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(uid).child("TimeTable")
        binding = ActivityStoreDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(applicationContext).load("https://giphy.com/gifs/wait-loading-attente-hWZBZjMMuMl7sWe0x8").into(binding.loadingGif)
        getTimeTable()
        getAssignments()
        getAssessments()
    }

    private fun getTimeTable() {
        for(day in days){
            readData(day)
        }
    }

    private fun saveTimeTable(day:String) {
        sharedPreferences = getSharedPreferences("TimeTable",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(classesList).toString()
        editor.putString("TT$day",json)
        editor.apply()
        classesList.clear()
    }

    private fun getAssignments(){
        firebaseReference.collection(uid+"Assignment")
            .orderBy("assignmentDeadline", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { Assignments ->
                for (assignment in Assignments) {
                    assignments.add(
                        Assignment(
                        assignment.getString("assignmentTitle").toString(),
                        assignment.getString("assignmentSubject").toString(),
                        assignment.getString("assignmentDeadline").toString()
                    ))
                }
                saveAssignments()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun getAssessments(){
        firebaseReference.collection(uid+"Assessment")
            .orderBy("assessmentDeadline", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { Assessments ->
                for (assessment in Assessments) {
                    assessments.add(Assessment(
                        assessment.getString("assessmentTitle").toString(),
                        assessment.getString("assessmentSubject").toString(),
                        assessment.getString("assessmentDeadline").toString()
                    ))
                }
                saveAssessments()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveAssignments(){
        sharedPreferences = getSharedPreferences("Assignments",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(assignments).toString()
        editor.putString("assignment list",json)
        editor.apply()
    }

    private fun saveAssessments(){
        sharedPreferences = getSharedPreferences("Assessments",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(assessments).toString()
        editor.putString("assessment list",json)
        editor.apply()
        launchHomeScreen(uid,name)
    }

    private fun launchHomeScreen(uid: String, Name:String) {
        val intent = Intent(applicationContext, HomeScreen::class.java)
        intent.putExtra("UID",uid)
        intent.putExtra("NAME",Name)
        finishAffinity()// clearing activity stack
        startActivity(intent)
    }

    private fun readData(day: String) {

        val morningClassesRef = timetableDatabase.child(day).child("AM")
        val afternoonClassesRef = timetableDatabase.child(day).child("PM")

        //morning classes data
        morningClassesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                classesList.clear()
                for (dataSnapShot in snapshot.children) {
                    classesList.add(
                        Time(
                            dataSnapShot.key.toString(),
                            dataSnapShot.value.toString(),
                            "-"
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "FAILED", Toast.LENGTH_SHORT).show()
            }
        })

        //evening class data
        afternoonClassesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapShot in snapshot.children) {
                    classesList.add(
                        Time(
                            dataSnapShot.key.toString(),
                            dataSnapShot.value.toString(),
                            "-"
                        )
                    )
                }
                saveTimeTable(day)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "FAILED", Toast.LENGTH_SHORT).show()
            }
        })
    }

}