package com.himanshu.codes.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
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
import com.himanshu.codes.screens.AddAssignment

class AssignmentsHome(private val UID: String) : Fragment() {

    private var assignments: ArrayList<Assignment> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private val firebaseReference = Firebase.firestore
    private lateinit var adapter: AssignmentAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_assignments_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.showAssignmentRecyclerView)
        loadAssignments()

        val addButton = view.findViewById<ImageView>(R.id.addAssignmentButton)
        val viewButton = view.findViewById<ImageView>(R.id.showAssignmentButton)

        addButton.setOnClickListener {
            val intent = Intent(context,AddAssignment::class.java)
            result.launch(intent)
        }

        viewButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.home_nav_container, CheckedAssignment(UID)).commit()
        }

    }

    private val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val assignment = it.data!!.getSerializableExtra("Assignment") as Assignment
        addAssignment(assignment)
    }

    private fun addAssignment(assignment: Assignment) {
        firebaseReference.collection("${UID}Assignment").add(assignment)
        var count = 0
        for(i in assignments){
            if(i.getAssignmentDeadline()>assignment.getAssignmentDeadline()){
                assignments.add(count,assignment)
            }
            else{
                count += 1
            }
        }
        clearSharedPreferences()
        writeInSharedPreferences()
        updateRecyclerView(count)
    }

    private fun loadAssignments() {
        ////Toast.makeText(context, "Inside Load Assignments", //Toast.LENGTH_SHORT).show()
        sharedPreferences = activity?.getSharedPreferences("Assignments", Context.MODE_PRIVATE)!!
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Assignment>>() {}.type
        val json = sharedPreferences.getString("assignment list", null)
        assignments = gson.fromJson(json, type)
        if (assignments.size == 0) {
            assignments = ArrayList()
        }
        //Toast.makeText(context, assignments.size.toString(), //Toast.LENGTH_SHORT).show()
        loadRecyclerView()
    }

    private fun loadRecyclerView() {

        //Toast.makeText(context, "Load Recycler View", //Toast.LENGTH_SHORT).show()
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AssignmentAdapter(assignments, assignmentDataPass, false)
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

        //Toast.makeText(context, "Clear Shared Preference", //Toast.LENGTH_SHORT).show()
        val editor = sharedPreferences.edit()
        editor.clear().apply()
    }

    private fun writeInSharedPreferences() {

        //Toast.makeText(context, "Write in shared preference", //Toast.LENGTH_SHORT).show()
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(assignments)
        //Toast.makeText(context, "save assignment\n$json", //Toast.LENGTH_SHORT).show()
        editor.putString("assignment list", json)
        editor.apply()
    }

    private fun updateRecyclerView(position: Int) {

        //Toast.makeText(context, "Update Recycler View", //Toast.LENGTH_SHORT).show()
        recyclerView.adapter?.notifyItemRemoved(position)
    }

    private fun updateFireStore(position: Int) {

        //Toast.makeText(context, "Update Firestore", //Toast.LENGTH_SHORT).show()
        updateCheckedAssignmentsList(position)
        updateUncheckedAssignmentsList(position)
    }

    private fun updateCheckedAssignmentsList(position: Int) {
        //Toast.makeText(context, "Update Checked Assignment", //Toast.LENGTH_SHORT).show()
        firebaseReference.collection("${UID}Completed Assignment").add(assignments[position])
    }

    private fun updateUncheckedAssignmentsList(position: Int) {

        //Toast.makeText(context, "Update Unchecked Assignment", //Toast.LENGTH_SHORT).show()
        firebaseReference.collection("${UID}Assignment").get().addOnSuccessListener {
            for (assessment in it) {
                if (assessment.getString("assignmentTitle")
                        .toString() == assignments[position].getAssignmentTitle() &&
                    assessment.getString("assignmentSubject")
                        .toString() == assignments[position].getAssignmentSubject()
                ) {
                    firebaseReference.collection("${UID}Assignment").document(assessment.id)
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