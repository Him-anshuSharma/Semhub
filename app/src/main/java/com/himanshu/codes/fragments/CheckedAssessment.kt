package com.himanshu.codes.fragments

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
import com.himanshu.codes.R
import com.himanshu.codes.adapters.AssessmentAdapter
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.interFace.AssignRecViewDataPass

class CheckedAssessment(private val UID: String) : Fragment() {

    private val firebaseReference = Firebase.firestore
    private val checkedAssessments: ArrayList<Assessment> = ArrayList()
    private lateinit var recyclerView: RecyclerView


    private val assessmentDataPass = object : AssignRecViewDataPass {
        override fun pass(position: Int) {
            updateList(position)
        }
    }

    private var adapter: AssessmentAdapter =
        AssessmentAdapter(checkedAssessments, assessmentDataPass, true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        loadData("${UID}Completed Assessment")
        return inflater.inflate(R.layout.fragment_checked_assessment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.showAssessmentRecyclerView)
    }


    private fun updateList(pos: Int) {
        removeCompletedAssessment(pos)
    }

    private fun removeCompletedAssessment(pos: Int) {

        firebaseReference.collection("${UID}Assessment").add(checkedAssessments[pos])

        firebaseReference.collection("${UID}Completed Assessment").get().addOnSuccessListener {
            for (Assessment in it) {
                if (Assessment.getString("assessmentTitle")
                        .toString() == checkedAssessments[pos].getAssessmentTitle() &&
                    Assessment.getString("assessmentSubject")
                        .toString() == checkedAssessments[pos].getAssessmentSubject()
                ) {


                    firebaseReference.collection("${UID}Completed Assessment")
                        .document(Assessment.id).delete()
                        .addOnSuccessListener {
                            checkedAssessments.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    break
                }
            }
        }
    }

    private fun loadData(_collection: String) {

        checkedAssessments.clear()

        firebaseReference.collection(_collection)
            .orderBy("assessmentDeadline", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { Assessments ->
                for (Assessment in Assessments) {
                    checkedAssessments.add(Assessment(
                        Assessment.getString("assessmentTitle").toString(),
                        Assessment.getString("assessmentSubject").toString(),
                        Assessment.getString("assessmentDeadline").toString()
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
        adapter = AssessmentAdapter(checkedAssessments, assessmentDataPass, true)
        recyclerView.adapter = adapter
    }

}