package com.himanshu.codes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.himanshu.codes.R
import com.himanshu.codes.adapters.AssignmentAdapter
import com.himanshu.codes.data.Assignment
import com.himanshu.codes.interFace.AssignRecViewDataPass

class CheckedAssignment(private val UID: String) : Fragment() {


    private val firebaseReference = Firebase.firestore
    private val checkedAssignments: ArrayList<Assignment> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    private val assignmentDataPass = object : AssignRecViewDataPass {
        override fun pass(position: Int) {
            updateList(position)
        }
    }

    private var adapter: AssignmentAdapter = AssignmentAdapter(checkedAssignments,assignmentDataPass,true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        loadData("${UID}Completed Assignment")
        return inflater.inflate(R.layout.fragment_checked_assignment_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.showAssignmentRecyclerView)

    }


    private fun updateList(pos:Int){
        removeCompletedAssignment(pos)
    }



    private fun removeCompletedAssignment(pos: Int) {

        firebaseReference.collection("${UID}Assignment").add(checkedAssignments[pos])

        firebaseReference.collection("${UID}Completed Assignment").get().addOnSuccessListener {
            for(assignment in it){
                if(assignment.getString("assignmentTitle").toString() == checkedAssignments[pos].getAssignmentTitle() &&
                    assignment.getString("assignmentSubject").toString() == checkedAssignments[pos].getAssignmentSubject()){


                    firebaseReference.collection("${UID}Completed Assignment").document(assignment.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(context,"Unchecked the assignment", Toast.LENGTH_SHORT).show()
                            checkedAssignments.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show()
                        }
                    break
                }
            }
        }


    }

    private fun loadData(_collection:String) {

        checkedAssignments.clear()

        firebaseReference.collection(_collection)
            .orderBy("assignmentDeadline", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { assignments ->
                for (assignment in assignments) {
                    checkedAssignments.add(Assignment(
                        assignment.getString("assignmentTitle").toString(),
                        assignment.getString("assignmentSubject").toString(),
                        assignment.getString("assignmentDeadline").toString()
                    ))
                }
                loadRecyclerView()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

    }

    private fun loadRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AssignmentAdapter(checkedAssignments,assignmentDataPass,true)
        recyclerView.adapter = adapter
    }

}