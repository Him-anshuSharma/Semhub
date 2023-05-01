package com.himanshu.codes.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.himanshu.codes.R
import com.himanshu.codes.ui.adapters.AssessmentAdapter
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.interFace.AssignRecViewDataPass

class CheckedAssessment(private val UID: String) : Fragment() {

    private var assessments: ArrayList<Assessment> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private val firebaseReference = Firebase.firestore
    private lateinit var adapter: AssessmentAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.checkedAssessmentRecyclerView)
        loadAssessments()
    }

    private fun loadAssessments() {
        sharedPreferences = activity?.getSharedPreferences("Completed Assessments", Context.MODE_PRIVATE)!!
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Assessment>>() {}.type
        val json = sharedPreferences.getString("completed assessment list", null)
        assessments = gson.fromJson(json, type)
        if (assessments.size == 0) {
            assessments = ArrayList()
        }
        //Toast.makeText(context, assessments.size.toString(), //Toast.LENGTH_SHORT).show()
        loadRecyclerView()
    }

    private fun loadRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AssessmentAdapter(assessments, assessmentDataPass, true)
        recyclerView.adapter = adapter
    }

    private val assessmentDataPass = object : AssignRecViewDataPass {
        override fun pass(position: Int) {
            updateAssessmentList(position)
        }
    }

    private fun updateAssessmentList(position: Int) {
        clearSharedPreferences()
        assessments.removeAt(position)
        writeInSharedPreferences()
        updateRecyclerView(position)
        updateFireStore(position)
    }

    private fun clearSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear().apply()
    }

    private fun writeInSharedPreferences() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(assessments)
        ////Toast.makeText(context, "save assessment\n$json", //Toast.LENGTH_SHORT).show()
        editor.putString("completed assessment list", json)
        editor.apply()
    }

    private fun updateRecyclerView(position: Int) {
        recyclerView.adapter?.notifyItemRemoved(position)
    }

    private fun updateFireStore(position: Int) {
        updateCheckedAssessmentsList(position)
        updateUncheckedAssessmentsList(position)
    }

    private fun updateUncheckedAssessmentsList(position: Int) {
        firebaseReference.collection("${UID}Assessment").add(assessments[position])
    }

    private fun updateCheckedAssessmentsList(position: Int) {
        firebaseReference.collection("${UID}Completed Assessment").get().addOnSuccessListener {
            for (assessment in it) {
                if (assessment.getString("assessmentTitle")
                        .toString() == assessments[position].getAssessmentTitle() &&
                    assessment.getString("assessmentSubject")
                        .toString() == assessments[position].getAssessmentSubject()
                ) {
                    firebaseReference.collection("${UID}Completed Assessment").document(assessment.id)
                        .delete()
                        .addOnSuccessListener {
                            //Toast.makeText(context, "Success", //Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            //Toast.makeText(context, "Failed", //Toast.LENGTH_SHORT).show()
                        }
                    break
                }
            }
        }
    }

}