package com.himanshu.codes.repository

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.himanshu.codes.dataFiles.*

class firebase_repository {

    private val firebaseReference = repository_reference.getReference()
    private val assessments:ArrayList<Assessment> = ArrayList()
    private val assignments:ArrayList<Assignment> = ArrayList()

    fun getAllAssessments():ArrayList<Assessment>{
        val ref = firebaseReference.getReference(UserDetails.getUid()!!).child("Assessment")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                assessments.clear()
                if(snapshot.value == null){
                    return
                }
                val _assignments = snapshot.value as Map<String, Map<String, String>>
                for ((_, value) in _assignments) {
                    val assignment = Assessment(
                        value["assessmentTitle"] ?: "",
                        value["assessmentSubject"] ?: "",
                        value["assessmentDeadline"] ?: ""
                    )
                    assessments.add(assignment)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Assessment Error",error.message)
            }
        })
        return assessments
    }

    fun getAllAssignments():ArrayList<Assignment>{
        val ref = firebaseReference.getReference(UserDetails.UID!!).child("Assignment")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                assignments.clear()
                if(snapshot.value == null){
                    return
                }
                val _assignments:Map<String, Map<String, String>>  = snapshot.value as Map<String, Map<String, String>>
                Log.d("Him", _assignments.toString())
                for ((_, value) in _assignments) {
                    val assignment = Assignment(
                        value["assignmentTitle"] ?: "",
                        value["assignmentSubject"] ?: "",
                        value["assignmentDeadline"] ?: ""
                    )
                    assignments.add(assignment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Assignment Error",error.message)

            }

        })
        return assignments
    }
    fun getWeekTimeTable():ArrayList<_Class>{
        return ArrayList()
    }
    fun addAssessment():Boolean{
        return true
    }
    fun addAssignment():Boolean{
        return true
    }
    fun removeAssignment():Boolean{
        return true
    }
    fun removeAssessment():Boolean{
        return true
    }
    fun editAssessment():Boolean{
        return true
    }
    fun editAssignment():Boolean{
        return true
    }
}