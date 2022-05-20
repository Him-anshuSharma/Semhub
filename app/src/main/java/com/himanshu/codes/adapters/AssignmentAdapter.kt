package com.himanshu.codes.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.codes.R
import com.himanshu.codes.dataFiles.Assignment
import com.himanshu.codes.interFace.AssignRecViewDataPass
import java.text.SimpleDateFormat
import java.util.*

class AssignmentAdapter(private val Assignments: ArrayList<Assignment>,
                        private val passData: AssignRecViewDataPass,
                        private val checked: Boolean
) :
    RecyclerView.Adapter<AssignmentAdapter.ViewHolder>() {

    private val sdf = SimpleDateFormat("y-MM-d")
    private val date: String = sdf.format(Date()).toString()

    /* Provide a reference to the type of views that you are using
    * (custom ViewHolder).
    */

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkboxAssignment)
        val assignmentTitle: TextView = view.findViewById(R.id.assignmentCardTitle)
        val assignmentSubject: TextView = view.findViewById(R.id.assignmentCardSubject)
        val assignmentDeadline: TextView = view.findViewById(R.id.assignmentCardDeadline)

        init{
            checkBox.setOnClickListener {
                passData.pass(adapterPosition)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.assignment_card, viewGroup, false)
        //Toast.makeText(viewGroup.context,date,Toast.LENGTH_SHORT).show()
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.assignmentTitle.text = Assignments[position].getAssignmentTitle()
        viewHolder.assignmentDeadline.text = Assignments[position].getAssignmentDeadline()
        viewHolder.assignmentSubject.text = Assignments[position].getAssignmentSubject()
        if(date>Assignments[position].getAssignmentDeadline()){
            viewHolder.assignmentDeadline.setTextColor(Color.RED)
            viewHolder.assignmentTitle.setTextColor(Color.RED)
            viewHolder.assignmentSubject.setTextColor(Color.RED)
        }

        if(checked){
            viewHolder.checkBox.isChecked = true
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = Assignments.size
}