package com.himanshu.codes.fragments

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.himanshu.codes.R
import com.himanshu.codes.adapters.AssignmentAdapter
import com.himanshu.codes.dataFiles.Assignment
import com.himanshu.codes.interFace.AssignRecViewDataPass

class CheckedAssignment(private val UID: String) : Fragment() {

    private var assignments: ArrayList<Assignment> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private val firebaseReference = Firebase.firestore
    private lateinit var adapter: AssignmentAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checked_assignment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.checkedAssignmentRecyclerView)
    }

    override fun onStart() {
        super.onStart()
        loadAssignments()
    }
    private fun loadAssignments() {
        sharedPreferences = activity?.getSharedPreferences("Completed Assignments", Context.MODE_PRIVATE)!!
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Assignment>>() {}.type
        val json = sharedPreferences.getString("completed assignment list", null)

        Toast.makeText(context, json, Toast.LENGTH_SHORT).show()
        assignments = gson.fromJson(json, type)
        if (assignments.size == 0) {
            assignments = ArrayList()
        }
        //Toast.makeText(context, assignments.size.toString(), //Toast.LENGTH_SHORT).show()
        loadRecyclerView()
    }

    private fun loadRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AssignmentAdapter(assignments, assignmentDataPass, true)
        recyclerView.adapter = adapter
    }
    private val assignmentDataPass = object : AssignRecViewDataPass {
        override fun pass(position: Int) {
            updateAssignmentList(position)
        }
    }

    private fun updateAssignmentList(position: Int) {

        //Toast.makeText(context, "Update Assignment List", //Toast.LENGTH_SHORT).show()
        clearSharedPreferences()
        assignments.removeAt(position)
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
        val json = gson.toJson(assignments)
        //Toast.makeText(context, "save assignment\n$json", //Toast.LENGTH_SHORT).show()
        editor.putString("assignment list", json)
        editor.apply()
    }

    private fun updateRecyclerView(position: Int) {
        recyclerView.adapter?.notifyItemRemoved(position)
    }

    private fun updateFireStore(position: Int) {
        updateCheckedAssignmentsList(position)
        updateUncheckedAssignmentsList(position)
    }

    private fun updateCheckedAssignmentsList(position: Int) {
        firebaseReference.collection("${UID}Completed Assignment").get().addOnSuccessListener {
            for (assessment in it) {
                if (assessment.getString("assignmentTitle")
                        .toString() == assignments[position].getAssignmentTitle() &&
                    assessment.getString("assignmentSubject")
                        .toString() == assignments[position].getAssignmentSubject()
                ) {
                    firebaseReference.collection("${UID}Completed Assignment")
                        .document(assessment.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    break
                }
            }
        }
    }
    private fun updateUncheckedAssignmentsList(position: Int) {
        firebaseReference.collection("${UID}Assignment").add(assignments[position])
    }
}