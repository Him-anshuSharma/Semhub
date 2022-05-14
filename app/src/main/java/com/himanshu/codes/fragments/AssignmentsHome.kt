package com.himanshu.codes.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.himanshu.codes.R
import com.himanshu.codes.interFace.AssignRecViewDataPass
import com.himanshu.codes.adapters.AssignmentAdapter
import com.himanshu.codes.assignment.Assignment
import com.himanshu.codes.screens.AddAssignment

class AssignmentsHome(private val UID: String) : Fragment() {

    private val firebaseReference = Firebase.firestore
    private val _assignments: ArrayList<Assignment> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    private val assignmentDataPass = object : AssignRecViewDataPass {
        override fun pass(position: Int) {
            updateList(position)
        }
    }

    private var adapter: AssignmentAdapter = AssignmentAdapter(_assignments,assignmentDataPass,false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        loadData(git "Assignment")
        return inflater.inflate(R.layout.fragment_assignments_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.showAssignmentRecyclerView)

        val add: ImageView = view.findViewById(R.id.addAssignmentButton)
        val show: ImageView = view.findViewById(R.id.showAssignmentButton)

        add.setOnClickListener {
            val intent = Intent(context, AddAssignment::class.java)
            intent.putExtra("UID",UID)
            getResult.launch(intent)
        }

        show.setOnClickListener{
            replace(CheckedAssignment(UID))
        }
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Toast.makeText(context,"Result ok",Toast.LENGTH_SHORT).show()
                var count = 0
                val assignment: Assignment =
                    it.data?.getSerializableExtra("Assignment") as Assignment
                for (i in _assignments) {
                    if (i.getAssignmentDeadline() > assignment.getAssignmentDeadline()) {
                        _assignments.add(count, assignment)
                        Log.d("ASSIGNMENT_ADD","RESULT_OK")
                        Toast.makeText(context, count.toString(), Toast.LENGTH_SHORT).show()
                        break
                    } else {
                        count++
                    }
                }
                adapter.notifyItemChanged(count)
            }
        }


    private fun updateList(pos:Int){
        addCompletedAssignment(pos)
    }



    private fun addCompletedAssignment(pos: Int) {

        firebaseReference.collection("${UID}Completed Assignment").add(_assignments[pos])

        firebaseReference.collection("${UID}Assignment").get().addOnSuccessListener{
            for(assignment in it)
            {
                if(assignment.getString("assignmentTitle").toString() == _assignments[pos].getAssignmentTitle() &&
                    assignment.getString("assignmentSubject").toString() == _assignments[pos].getAssignmentSubject())
                    {
                    firebaseReference.collection("${UID}Assignment").document(assignment.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(context,"Checked assignment",Toast.LENGTH_SHORT).show()
                            _assignments.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                        }
                    Toast.makeText(context,id,Toast.LENGTH_SHORT).show()
                    break
                }
            }
        }
    }

    private fun loadData(_collection:String) {
        _assignments.clear()
        firebaseReference.collection(UID+_collection)
            .orderBy("assignmentDeadline", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { assignments ->
                for (assignment in assignments) {
                    _assignments.add(Assignment(
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
        adapter = AssignmentAdapter(_assignments,assignmentDataPass,false)
        recyclerView.adapter = adapter
    }

    private fun replace(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().addToBackStack("CheckAssignment").replace(R.id.home_nav_container, fragment).commit()
    }

}