package com.himanshu.codes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.himanshu.codes.R
import com.himanshu.codes.adapters.TimeTableAdapter
import com.himanshu.codes.time.Time
import java.util.*

class TimeTable(private val UID: String) : Fragment() {

    //adapter
    private lateinit var adapter: TimeTableAdapter

    //store classes list
    private val classesList: ArrayList<Time> = ArrayList()

    //Get Data Timetable Node
    private val database = FirebaseDatabase.getInstance().reference.child("Users").child(UID).child("TimeTable")

    //Creating View for fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_time_table, container, false)
    }


    //Perform operations when view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load class data for current day
        loadCurrentDayClasses()

        //bottom navigation bar
        val dayNavBar: BottomNavigationView = view.findViewById(R.id.days_nav_bar)

        //on day selected
        dayNavBar.setOnItemSelectedListener {
            //clear previous data
            classesList.clear()

            //clearing recycler View
            adapter.notifyDataSetChanged()

            //fetch clicked day's class data
            loadClasses(it.itemId)
            true
        }

        //
    }

    private fun loadCurrentDayClasses() {

        val bar = view?.findViewById<BottomNavigationView>(R.id.days_nav_bar)!!

        //get current day
        when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> {
                bar.selectedItemId = R.id.timetable_menu_monday
                loadClasses(R.id.timetable_menu_monday)
            }
            Calendar.TUESDAY -> {
                bar.selectedItemId = R.id.timetable_menu_tuesday
                loadClasses(R.id.timetable_menu_tuesday)
            }
            Calendar.WEDNESDAY -> {
                bar.selectedItemId = R.id.timetable_menu_wednesday
                loadClasses(R.id.timetable_menu_wednesday)
            }
            Calendar.THURSDAY -> {
                bar.selectedItemId = R.id.timetable_menu_thursday
                loadClasses(R.id.timetable_menu_thursday)
            }
            Calendar.FRIDAY -> {
                bar.selectedItemId = R.id.timetable_menu_friday
                loadClasses(R.id.timetable_menu_friday)
            }
            else -> {
                loadClasses(R.id.timetable_menu_monday)
            }
        }
    }

    //Load day-wise Class data
    private fun loadClasses(itemId: Int) {
        when (itemId) {
            R.id.timetable_menu_monday -> {
                readData("Monday")
            }
            R.id.timetable_menu_tuesday -> {
                readData("Tuesday")
            }
            R.id.timetable_menu_wednesday -> {
                readData("Wednesday")
            }
            R.id.timetable_menu_thursday -> {
                readData("Thursday")
            }
            R.id.timetable_menu_friday -> {
                readData("Friday")
            }
        }
    }

    //read day-wise class data
    private fun readData(day: String) {

        val morningClassesRef = database.child(day).child("AM")
        val afternoonClassesRef = database.child(day).child("PM")

        //morning classes data
        morningClassesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapShot in snapshot.children) {

                    classesList.add(
                        Time(
                            dataSnapShot.key.toString(),
                            dataSnapShot.value.toString(),
                            "-"
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show()
            }
        })

        //evening class data
        afternoonClassesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapShot in snapshot.children) {
                    classesList.add(
                        Time(
                            dataSnapShot.key.toString(),
                            dataSnapShot.value.toString(),
                            "-"
                        )
                    )
                }
                //set recyclerview after reading data
                setRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //set the recycler view
    private fun setRecyclerView() {
        val recyclerView: RecyclerView = view?.findViewById(R.id.TimeTableRecyclerView)!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TimeTableAdapter(classesList)
        recyclerView.adapter = adapter
    }
}