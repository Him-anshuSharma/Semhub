package com.himanshu.codes.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.himanshu.codes.R
import com.himanshu.codes.adapters.AssessmentAdapter
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.interFace.AssignRecViewDataPass
import com.himanshu.codes.screens.AddAssessment


class AssessmentsHome(private val UID: String) : Fragment() {

    private val firebaseReference = Firebase.firestore
    private var _assessment: ArrayList<Assessment> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    private lateinit var sharedPreferences: SharedPreferences

    private val assessmentDataPass = object : AssignRecViewDataPass {
        override fun pass(position: Int) {
            updateList(position)
        }

    }

    private var adapter: AssessmentAdapter =
        AssessmentAdapter(_assessment, assessmentDataPass, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        loadData()
        return inflater.inflate(R.layout.fragment_assesments_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.showAssessmentRecyclerView)

        val add: ImageView = view.findViewById(R.id.addAssessmentButton)
        val show: ImageView = view.findViewById(R.id.showAssessmentButton)

        add.setOnClickListener {
            val intent = Intent(context, AddAssessment::class.java)
            intent.putExtra("UID", UID)
            getResult.launch(intent)
        }

        show.setOnClickListener {
            replace(CheckedAssessment(UID))
        }
    }

    private fun loadData() {
        sharedPreferences = activity?.getSharedPreferences("Assessments", Context.MODE_PRIVATE)!!
        val gson = Gson()
        val json = sharedPreferences.getString("assessment list",null)
        Toast.makeText(context,"Assessment" + json.toString(),Toast.LENGTH_SHORT).show()
        val type = object : TypeToken<ArrayList<Assessment>>(){}.type
        _assessment = try {
            gson.fromJson(json, type)
        }catch (e:Exception) {
            ArrayList()
        }
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                var count = 0
                val assessment: Assessment =
                    it.data?.getSerializableExtra("Assessment") as Assessment
                for (i in _assessment) {
                    if (i.getAssessmentDeadline() > assessment.getAssessmentDeadline()) {
                        _assessment.add(count, assessment)
                        Log.d("ASSIGNMENT_ADD", "RESULT_OK")
                        break
                    } else {
                        count++
                    }
                }
                adapter.notifyItemChanged(count)
            }
        }

    private fun updateList(pos: Int) {
        addCompletedAssessment(pos)
    }

    private fun addCompletedAssessment(pos: Int) {

        firebaseReference.collection("${UID}Completed Assessment").add(_assessment[pos])

        firebaseReference.collection("${UID}Assessment").get().addOnSuccessListener {
            for (assessment in it) {
                if (assessment.getString("assessmentTitle")
                        .toString() == _assessment[pos].getAssessmentTitle() &&
                    assessment.getString("assessmentSubject")
                        .toString() == _assessment[pos].getAssessmentSubject()
                ) {
                    firebaseReference.collection("${UID}Assessment").document(assessment.id)
                        .delete()
                        .addOnSuccessListener {
                            _assessment.removeAt(pos)
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

    private fun loadRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AssessmentAdapter(_assessment, assessmentDataPass, false)
        recyclerView.adapter = adapter

    }


    private fun replace(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().addToBackStack("checkAssessment")
            .replace(R.id.home_nav_container, fragment).commit()
    }
}
