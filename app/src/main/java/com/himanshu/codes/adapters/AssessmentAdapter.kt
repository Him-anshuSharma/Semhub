package com.himanshu.codes.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.codes.R
import com.himanshu.codes.dataFiles.Assessment
import com.himanshu.codes.interFace.AssignRecViewDataPass
import java.text.SimpleDateFormat
import java.util.*

class AssessmentAdapter(private val Assessments: ArrayList<Assessment>,
                        private val passData: AssignRecViewDataPass,
                        private val checked: Boolean
                        ):RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {


    private val sdf = SimpleDateFormat("y-MM-d")
    private val date: String = sdf.format(Date()).toString()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkboxAssessment)
        val assessmentTitle: TextView = view.findViewById(R.id.assessmentCardTitle)
        val assessmentSubject: TextView = view.findViewById(R.id.assessmentCardSubject)
        val assessmentDeadline: TextView = view.findViewById(R.id.assessmentCardDeadline)

        init {
            checkBox.setOnClickListener{
                passData.pass(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.assessment_card,viewGroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.assessmentTitle.text = Assessments[position].getAssessmentTitle()
        viewHolder.assessmentDeadline.text = Assessments[position].getAssessmentDeadline()
        viewHolder.assessmentSubject.text = Assessments[position].getAssessmentSubject()
        if(date>Assessments[position].getAssessmentDeadline()){
            viewHolder.assessmentDeadline.setTextColor(Color.RED)
            viewHolder.assessmentTitle.setTextColor(Color.RED)
            viewHolder.assessmentSubject.setTextColor(Color.RED)
        }
        if(checked){
            viewHolder.checkBox.isChecked = true
        }
    }

    override fun getItemCount(): Int = Assessments.size
}