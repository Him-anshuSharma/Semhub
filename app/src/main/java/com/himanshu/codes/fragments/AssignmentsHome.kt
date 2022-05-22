package com.himanshu.codes.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.himanshu.codes.R
import com.himanshu.codes.adapters.AssignmentAdapter
import com.himanshu.codes.dataFiles.Assignment
import com.himanshu.codes.interFace.AssignRecViewDataPass

class AssignmentsHome(private val UID: String) : Fragment() {

    private var assignments: ArrayList<Assignment> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    private lateinit var sharedPreferences: SharedPreferences

    private val assignmentDataPass = object : AssignRecViewDataPass {
        override fun pass(position: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView = view?.findViewById(R.id.showAssignmentRecyclerView)!!
        loadAssignments()

    }

    private fun loadAssignments() {
        sharedPreferences = activity?.getSharedPreferences("Assignments",Context.MODE_PRIVATE)!!
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Assignment>>(){}.type
        val json = sharedPreferences.getString("assignment list",null)
        assignments = gson.fromJson(json,type)
        if(assignments == null){
            assignments = ArrayList()
        }
        loadRecyclerView()
    }

    private fun loadRecyclerView() {
        val adapter = AssignmentAdapter(assignments,assignmentDataPass,false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

}