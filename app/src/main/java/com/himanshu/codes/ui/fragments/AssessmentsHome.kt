package com.himanshu.codes.ui.fragments

import android.content.Intent
import android.os.Bundle
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
import com.himanshu.codes.ui.adapters.AssessmentAdapter
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.interFace.AssignRecViewDataPass
import com.himanshu.codes.ui.screens.AddAssessment


class AssessmentsHome(private val UID: String) : Fragment() {

    private var assessments: ArrayList<Assessment> = ArrayList()
    private val firebaseReference = Firebase.database
    private lateinit var adapter: AssessmentAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_assessments_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.showAssessmentRecyclerView)
        fetchAssessmentFromFirebase()
        loadRecyclerView()

        val addButton = view.findViewById<ImageView>(R.id.addAssessmentButton)

        addButton.setOnClickListener {
            val intent = Intent(context, AddAssessment()::class.java)
            intent.putExtra("UID", UID)
            startActivity(intent)
        }

    }

    private fun fetchAssessmentFromFirebase() {
        val ref = firebaseReference.getReference(UID).child("Assessment")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                assessments.clear()
                if(snapshot.value == null){
                    return
                }
                val _assignments = snapshot.value as Map<String, Map<String, String>>
                for ((id, value) in _assignments) {
                    Toast.makeText(context,id,Toast.LENGTH_SHORT).show()
                    val assignment = Assessment(
                        value["assessmentTitle"] ?: "",
                        value["assessmentSubject"] ?: "",
                        value["assessmentDeadline"] ?: "",
                        id
                    )
                    assessments.add(assignment)
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
        adapter = AssessmentAdapter(assessments, assessmentDataPass, false)
        recyclerView.adapter = adapter
    }

    private val assessmentDataPass = object : AssignRecViewDataPass {
        override fun pass(position: Int) {
            updateAssessmentList(position)
        }
    }

    private fun updateAssessmentList(position: Int) {
        Toast.makeText(context,assessments[position].id,Toast.LENGTH_SHORT).show()
        firebaseReference.getReference(UID).child("Assessment").child(assessments[position].id).removeValue()
        assessments.removeAt(position)
        updateRecyclerView(position)
    }


    private fun updateRecyclerView(position: Int) {
        recyclerView.adapter?.notifyItemRemoved(position)
    }

}
