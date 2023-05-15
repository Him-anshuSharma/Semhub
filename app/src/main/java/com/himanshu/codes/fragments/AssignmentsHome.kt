package com.himanshu.codes.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.himanshu.codes.R
import com.himanshu.codes.adapters.AssignmentAdapter
import com.himanshu.codes.dataFiles.Assignment
import com.himanshu.codes.dataFiles.UserDetails
import com.himanshu.codes.interFace.AssignRecViewDataPass
import com.himanshu.codes.screens.AddAssignment

class AssignmentsHome() : Fragment() {

    private var assignments: ArrayList<Assignment> = ArrayList()
    private val firebaseReference = Firebase.database
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
        fetchAssignmentsFromFirebase()
        loadRecyclerView()

        val addButton = view.findViewById<ImageView>(R.id.addAssignmentButton)
        val viewButton = view.findViewById<ImageView>(R.id.showAssignmentButton)

        addButton.setOnClickListener {
            val intent = Intent(context, AddAssignment()::class.java)
            //intent.putExtra("UID", UID)
            startActivity(intent)
        }

    }

    private fun fetchAssignmentsFromFirebase() {
        val ref = firebaseReference.getReference(UserDetails.UID.toString()).child("Assignment")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                assignments.clear()
                if(snapshot.value == null){
                    return
                }
                val _assignments:Map<String, Map<String, String>>  = snapshot.value as Map<String, Map<String, String>>
                Log.d("Him", _assignments.toString())
                for ((id, value) in _assignments) {
                    val assignment = Assignment(
                        value["assignmentTitle"] ?: "",
                        value["assignmentSubject"] ?: "",
                        value["assignmentDeadline"] ?: "",
                        id
                    )
                    assignments.add(assignment)
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to read value.", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun loadRecyclerView() {
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
        firebaseReference.getReference(UserDetails.UID.toString()).child("Assignment").child(assignments[position].id).removeValue()
        assignments.removeAt(position)
        updateRecyclerView(position)
    }

    private fun updateRecyclerView(position: Int) {

        recyclerView.adapter?.notifyItemRemoved(position)
    }


}